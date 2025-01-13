package com.example.petnet

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petnet.ui.theme.PetnetTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PetnetTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFEB6423)
                ) {
                    SignUp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUp() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = painterResource(id = R.drawable.paws2),
            contentDescription = "Paws Background",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.FillWidth
        )

        Text(
            text = "Create an account",
            color = Color.White,
            fontSize = 36.sp,
            fontFamily = balootamma,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 30.dp)
        )

        // Email/Username
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Username or Email", color = gr) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = wh,
                unfocusedContainerColor = wh,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = gr
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
            placeholder = { Text("Password", color = gr) },
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
                focusedContainerColor = wh,
                unfocusedContainerColor = wh,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = gr
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 21.dp)
                .height(64.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Confirm Password
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeholder = { Text("Confirm Password", color = gr) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = wh,
                unfocusedContainerColor = wh,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = gr
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 21.dp)
                .height(64.dp)
        )

        Button(
            onClick = {
                if (password == confirmPassword && email.isNotEmpty()) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val userId = auth.currentUser?.uid
                                if (userId != null) {
                                    val userProfile = mapOf(
                                        "username" to email.split("@")[0], // Default username
                                        "email" to email,
                                        "profilePictureUrl" to "", // Default profile picture URL
                                        "additionalInfo" to "No info provided"
                                    )

                                    FirebaseFirestore.getInstance()
                                        .collection("profiles")
                                        .document(userId)
                                        .set(userProfile)
                                        .addOnSuccessListener {
                                            Toast.makeText(context, "Account created successfully!", Toast.LENGTH_LONG).show()
                                            val intent = Intent(context, MainActivity::class.java)
                                            context.startActivity(intent)
                                        }
                                        .addOnFailureListener { exception ->
                                            Toast.makeText(context, "Failed to create profile: ${exception.message}", Toast.LENGTH_LONG).show()
                                        }
                                }
                            } else {
                                val errorMessage = when (task.exception?.message) {
                                    "The email address is already in use by another account." -> {
                                        "This email is already registered. Please log in or use a different email."
                                    }
                                    else -> "Sign-up failed: ${task.exception?.message}"
                                }
                                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                            }
                        }
                } else {
                    Toast.makeText(context, "Passwords do not match or fields are empty!", Toast.LENGTH_LONG).show()
                }
            },
            colors = ButtonDefaults.buttonColors(wh),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .padding(top = 24.dp)
                .height(56.dp),
            enabled = password == confirmPassword && password.isNotEmpty() && email.isNotEmpty()
        ) {
            Text(
                text = "Register",
                color = Color(0xFFEB6423),
                fontFamily = balootamma,
                fontSize = 24.sp
            )
        }


        Text(
            text = "Back",
            color = Color.White,
            fontSize = 20.sp,
            fontFamily = balootamma,
            modifier = Modifier
                .padding(top = 64.dp)
                .clickable {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                    (context as? ComponentActivity)?.finish()
                }
        )
    }
}
