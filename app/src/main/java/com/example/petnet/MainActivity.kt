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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.petnet.ui.theme.PetnetTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

val balootamma = FontFamily(Font(R.font.balootammaregular))
val nunito = FontFamily(Font(R.font.nunitosans))
val yellow = Color(0xFFF8E16C)
val bl = Color(0xFF323232)
val blu = Color(0xFFA3D9FF)
val org = Color(0xFFEB6423)
val wh = Color(0xFFFBFBFB)
val gr = Color(0xFF626262)
val red = Color(0xFFF8CBB4)

class MainActivity : ComponentActivity() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        installSplashScreen()
        super.onCreate(savedInstanceState)

        val currentUser = auth.currentUser
        if (currentUser != null) {
            // If the user is logged in, navigate to Main Feed
            val intent = Intent(this, MainFeedScreen::class.java)
            startActivity(intent)
            finish() // Close MainActivity so it won't be in the back stack
        } else {
            // If the user is not logged in, show the Login screen
            setContent {
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize(),
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
            fontSize = 36.sp,
            fontFamily = balootamma,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 80.dp, start = 8.dp)
        )

        // Email/Username
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = {
                Text(
                    "Username or Email", color = gr,
                    fontSize = 13.sp,
                    fontFamily = nunito,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.namebar),
                    contentDescription = null,
                    tint = gr,
                    modifier = Modifier.size(24.dp)
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFFBFBFB),
                unfocusedContainerColor = Color(0xFFFBFBFB),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = Color.Gray
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 21.dp)
                .height(64.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Password
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = {
                Text(
                    "Password", color = gr,
                    fontSize = 13.sp,
                    fontFamily = nunito,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.passwordbar),
                    contentDescription = null,
                    tint = gr,
                    modifier = Modifier.size(24.dp)
                )
            },
            trailingIcon = {
                val visibilityIcon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                Icon(
                    imageVector = visibilityIcon,
                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                    tint = gr,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { passwordVisible = !passwordVisible }
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFFBFBFB),
                unfocusedContainerColor = Color(0xFFFBFBFB),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = Color.Gray
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 21.dp)
                .height(64.dp)
        )

        // Forgot Password
        Text(
            text = "Forgot Password?",
            color = Color.White,
            fontSize = 14.sp,
            fontFamily = nunito,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 32.dp, bottom = 80.dp, end = 20.dp)
                .clickable {
                    val intent = Intent(context, ForgotPasswordScreen::class.java)
                    context.startActivity(intent)
                }
        )

        // Sign-In
        Button(
            onClick = {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(context, MainFeedScreen::class.java)
                            context.startActivity(intent)
                        } else {
                            // Handle error
                        }
                    }
            },
            colors = ButtonDefaults.buttonColors(containerColor = wh),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .padding(top = 24.dp)
                .height(56.dp)
        ) {
            Text(
                text = "Sign In",
                color = org,
                fontFamily = balootamma,
                fontSize = 24.sp
            )
        }

        // Sign-Up
        Text(
            text = buildAnnotatedString {
                append("Don't have an account yet? ")
                withStyle(
                    style = SpanStyle(
                        color = Color(0xFFFBFBFB),
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append("Sign Up")
                }
            },
            color = Color.White,
            fontSize = 14.sp,
            fontFamily = nunito,
            modifier = Modifier
                .padding(top = 64.dp)
                .clickable {
                    val intent = Intent(context, SignUpScreen::class.java)
                    context.startActivity(intent)
                }
        )
    }
}
