package com.example.cric

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.cric.ui.theme.CricTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Timestamp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : ComponentActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sessionManager: SessionManager
    private lateinit var isUserLoggedInState: MutableState<Boolean>
    private val firestore = FirebaseFirestore.getInstance()
    private val TAG = "MainActivity"


    companion object {
        const val ACCESS_CODE = "1234" // Your family access code here
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        firebaseAuth = FirebaseAuth.getInstance()
        sessionManager = SessionManager(this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val googleLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    if (account != null) {
                        Log.d(TAG, "Google SignIn Success: ${account.email}")
                        firebaseAuthWithGoogle(account.idToken!!)
                    }
                } catch (e: ApiException) {
                    Log.d(TAG, "Google SignIn Failed: ${e.statusCode}")
                }
            }

        setContent {
            CricTheme {
                isUserLoggedInState = remember { mutableStateOf(sessionManager.isLoggedIn()) }
                val navController = rememberNavController()
                var messages by remember { mutableStateOf(listOf<ChatMessage>()) }

                LaunchedEffect(isUserLoggedInState.value) {
                    if (isUserLoggedInState.value) {
                        listenToMessages { newMessages ->
                            messages = newMessages
                        }
                    }
                }

                AppNavigation(
                    googleSignInClient = googleSignInClient,
                    onGoogleSignInClick = {
                        val signInIntent = googleSignInClient.signInIntent
                        googleLauncher.launch(signInIntent)
                    },
                    isUserLoggedIn = isUserLoggedInState.value,
                    onLogout = {
                        firebaseAuth.signOut()
                        googleSignInClient.signOut()
                        sessionManager.clearSession()
                        isUserLoggedInState.value = false
                    },
                    sessionManager = sessionManager,
                    onGithubSignInClick = { launchGitHubLogin() },
                    messages = messages,
                    sendMessage = { text, imageUrl -> sendMessage(text, imageUrl) },
                    editMessage = { message, newText -> editMessage(message, newText) },    // <-- Added
                    deleteMessage = { message -> deleteMessage(message) }                  // <-- Added
                )
            }
        }

    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    Log.d(TAG, "Firebase Auth Success: ${user?.email}")

                    lifecycleScope.launch {
                        sessionManager.saveLoginState(true)
                        sessionManager.saveUserDetails(user?.displayName ?: "", user?.email ?: "")
                    }
                    isUserLoggedInState.value = true
                } else {
                    Log.w(TAG, "Firebase Auth Failed", task.exception)
                }
            }
    }

    private fun launchGitHubLogin() {
        val provider = OAuthProvider.newBuilder("github.com")

        val pending = firebaseAuth.pendingAuthResult
        if (pending != null) {
            pending
                .addOnSuccessListener { handleGitHubResult(it) }
                .addOnFailureListener { Log.e("GitHubLogin", "Pending error", it) }
        } else {
            firebaseAuth.startActivityForSignInWithProvider(this, provider.build())
                .addOnSuccessListener { handleGitHubResult(it) }
                .addOnFailureListener { Log.e("GitHubLogin", "GitHub login failed", it) }
        }
    }

    private fun handleGitHubResult(result: AuthResult) {
        val user = result.user
        val name = user?.displayName ?: "Anonymous"
        val email = user?.email ?: ""

        Log.d("GitHubLogin", "GitHub login success: $name <$email>")

        lifecycleScope.launch {
            sessionManager.saveLoginState(true)
            sessionManager.saveUserDetails(name, email)
            isUserLoggedInState.value = true
        }
    }


    private fun sendMessage(text: String, imageUrl: String? = null) {
        val user = firebaseAuth.currentUser ?: return

        val message = ChatMessage(
            text = text,
            senderId = user.uid,
            senderName = user.displayName ?: "Anonymous",
            timestamp = Timestamp.now(),
            imageUrl = imageUrl
        )

        firestore.collection("messages")
            .add(message)
            .addOnSuccessListener {
                Log.d(TAG, "Message sent successfully")


            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error sending message", e)
            }
    }

    fun sendMessageWithImage(text: String, imageUri: Uri) {
        uploadImageToFirebase(
            imageUri = imageUri,
            onUploadSuccess = { imageUrl ->
                sendMessage(text, imageUrl)
            },
            onFailure = { e ->
                Log.e(TAG, "Image upload failed: ${e.message}")
            }
        )
    }

    private fun uploadImageToFirebase(
        imageUri: Uri,
        onUploadSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val fileName = "images/${UUID.randomUUID()}.jpg"
        val storageRef = FirebaseStorage.getInstance().reference.child(fileName)

        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    onUploadSuccess(uri.toString())
                }.addOnFailureListener { e ->
                    onFailure(e)
                }
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    private fun editMessage(message: ChatMessage, newText: String) {
        if (message.messageId.isNotBlank()) {
            firestore.collection("messages")
                .document(message.messageId)
                .update("text", newText)
                .addOnSuccessListener { Log.d(TAG, "Message edited") }
                .addOnFailureListener { Log.e(TAG, "Edit failed", it) }
        }
    }

    private fun deleteMessage(message: ChatMessage) {
        if (message.messageId.isNotBlank()) {
            firestore.collection("messages")
                .document(message.messageId)
                .delete()
                .addOnSuccessListener { Log.d(TAG, "Message deleted") }
                .addOnFailureListener { Log.e(TAG, "Delete failed", it) }
        }
    }

    private fun listenToMessages(onMessagesReceived: (List<ChatMessage>) -> Unit) {
        firestore.collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w(TAG, "Listen failed.", error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val messages = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(ChatMessage::class.java)?.copy(messageId = doc.id)
                    }
                    onMessagesReceived(messages)
                }
            }
    }
}
