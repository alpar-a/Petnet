package com.example.petnet

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.petnet.ui.theme.PetnetTheme
import com.google.firebase.FirebaseApp

val balootamma = FontFamily(Font(R.font.balootammaregular))
val nunito = FontFamily(Font(R.font.nunitosans))
val yellow = Color(0xFFF8E16C)
val bl = Color(0xFF323232)
val blu = Color(0xFFA3D9FF)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            MainActivityContent()
            PetnetTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFEB6423)
                ) {
                    LoginScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = painterResource(id = R.drawable.paws),
            contentDescription = "Paws Background",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.FillWidth
        )

        // Welcome
        Text(
            text = "Welcome!",
            color = Color.White,
            fontSize = 32.sp,
            fontFamily = balootamma,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 80.dp, start = 8.dp)
        )

        // Email/Username
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = {
                Text("Username or Email", color = Color.Gray, fontSize = 18.sp)
            },
            leadingIcon = {
                Icon(Icons.Default.Email, contentDescription = null, tint = Color.Gray)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF8E16C),
                unfocusedContainerColor = Color(0xFFF8E16C),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = Color.Gray
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .height(64.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Password
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = {
                Text("Password", color = Color.Gray, fontSize = 18.sp)
            },
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Gray)
            },
            trailingIcon = {
                val visibilityIcon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                Icon(
                    imageVector = visibilityIcon,
                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { passwordVisible = !passwordVisible }
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF8E16C),
                unfocusedContainerColor = Color(0xFFF8E16C),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = Color.Gray
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .height(64.dp)
        )

        // Forgot Password
        Text(
            text = "Forgot Password?",
            color = Color.White,
            fontSize = 14.sp,
            fontFamily = balootamma,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 32.dp, bottom = 80.dp)
                .clickable {
                    val intent = Intent(context, ForgotPasswordScreen::class.java)
                    context.startActivity(intent) }
        )

        // Sign-In
        Button(
            onClick = { /* sign-in code */ },
            colors = ButtonDefaults.buttonColors(containerColor = yellow),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .height(56.dp)
        ) {
            Text(
                text = "Sign In",
                color = Color(0xFFEB6423),
                fontFamily = balootamma,
                fontSize = 24.sp
            )
        }

        Button(
            onClick = {
                val intent = Intent(context, MainFeedScreen::class.java)
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(containerColor = yellow),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .height(56.dp)
        ) {
            Text(
                text = "Direct to Feed",
                color = Color(0xFFEB6423),
                fontFamily = balootamma,
                fontSize = 24.sp
            )
        }

        // Sign-Up
        Text(
            text = buildAnnotatedString {
                append("Don't have an account yet? ")
                withStyle(style = SpanStyle(color = Color(0xFFF8E16C))) {
                    append("Sign Up")
                }
            },
            color = Color.White,
            fontSize = 14.sp,
            fontFamily = balootamma,
            modifier = Modifier
                .padding(top = 64.dp)
                .clickable {
                    val intent = Intent(context, SignUpScreen::class.java)
                    context.startActivity(intent)
                }
        )
    }
}
