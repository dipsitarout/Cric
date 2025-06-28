package com.example.cric

import androidx.compose.runtime.*
import androidx.navigation.compose.*
import com.example.cric8.HomeScreen
import com.example.cric8.ProfileScreen
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import androidx.navigation.NavHostController
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable

fun AppNavigation(
    googleSignInClient: GoogleSignInClient,
    onGoogleSignInClick: () -> Unit,
    isUserLoggedIn: Boolean,
    onLogout: () -> Unit,
    sessionManager: SessionManager,
    onGithubSignInClick: () -> Unit,
    messages: List<ChatMessage>,
    sendMessage: (String, String?) -> Unit,
    editMessage: (ChatMessage, String) -> Unit,
    deleteMessage: (ChatMessage) -> Unit,

) {
    val navController = rememberNavController()

    LaunchedEffect(isUserLoggedIn) {
        if (isUserLoggedIn) {
            navController.navigate("HomeScreen") {
                popUpTo("login") { inclusive = true }
            }
        } else {
            navController.navigate("login") {
                popUpTo("HomeScreen") { inclusive = true }
            }
        }
    }

    NavHost(navController = navController, startDestination = if (isUserLoggedIn) "HomeScreen" else "login") {
        composable("login") {
            SplashScreen(
                googleSignInClient = googleSignInClient,
                onGoogleSignInClick = onGoogleSignInClick,
                onGithubSignInClick = onGithubSignInClick
            )
        }

        composable("HomeScreen") {
            HomeScreen(navController = navController)
        }

        composable("fixtures") {
            Fixtures(navController = navController)
        }

        composable("profile") {
            ProfileScreen(
                navController = navController,
                onLogout = { onLogout() },
                sessionManager = sessionManager,

            )
        }

        composable("chatScreen") {
            ProtectedChatScreen(
                navController = navController,
                messages = messages,
                sendMessage = sendMessage,
                onLogout = onLogout,
                editMessage = editMessage,
                deleteMessage = deleteMessage
            )
        }

        composable("weather") {
            val weatherViewModel: WeatherViewModel = viewModel()
            WeatherScreen(
                viewModel = weatherViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
