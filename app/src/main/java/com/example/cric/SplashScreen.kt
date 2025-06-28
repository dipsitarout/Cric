package com.example.cric

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.auth.api.signin.GoogleSignInClient

@Composable
fun SplashScreen(
    googleSignInClient: GoogleSignInClient,
    onGoogleSignInClick: () -> Unit,
    onGithubSignInClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {

        // Top Yellow Background with Image
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFD54F))
                .height(650.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.splashcric),
                contentDescription = "Cricket Illustration",
                modifier = Modifier
                    .padding(bottom = 130.dp)
                    .padding(horizontal = 20.dp)
                    .padding(start = 57.dp, top = 47.dp)
                    .height(358.dp)
                    .width(378.dp)
            )
        }

        // White Curve Section with Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 420.dp)
                .clip(RoundedCornerShape(topStart = 200.dp, topEnd = 200.dp))
                .background(Color.White)
                .padding(horizontal = 24.dp, vertical = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(84.dp))

            Text(
                text = "Welcome to CRIC8",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Create an account to save your team,\nclub & league preferences.",
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(25.dp))

            // Google Sign-in Button
            Button(
                onClick = onGoogleSignInClick,
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.dp, Color.LightGray),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 8.dp,
                    disabledElevation = 0.dp
                ),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White,
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = "Google Icon",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Continue with Google")
            }

            Spacer(modifier = Modifier.height(16.dp))

// GitHub Sign-in Button
            Button(
                onClick = onGithubSignInClick,
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.dp, Color.LightGray),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 8.dp,
                    disabledElevation = 0.dp
                ),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White,
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.github),
                    contentDescription = "GitHub Icon",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Continue with GitHub")
            }
        }
    }
}
