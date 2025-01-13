package com.example.petnet

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import coil.compose.AsyncImagePainter.State.Empty.painter
import com.example.petnet.ui.theme.PetnetTheme

@OptIn(ExperimentalMaterial3Api::class)
class DeleteAccountScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PetnetTheme {
                Scaffold(
                    content = { padding ->
                        DeleteAccountContent(modifier = Modifier.padding(padding))
                    }
                )
            }
        }
    }
}

@Composable
fun DeleteAccountContent(modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFEB6423)) // Orange background
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Back Icon
        IconButton(
            onClick = { /* Navigate back action */ },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_back), // Replace with your back icon resource
                contentDescription = "Back",
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bunny Icon
        Image(
            painter = painterResource(R.drawable.bunny),
            contentDescription = "Bunny Icon",
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Title
        Text(
            text = "Delete Account",
            fontSize = 24.sp,
            fontFamily = balootamma,
            color = Color.White,
            textAlign = TextAlign.Center

        )

        Spacer(modifier = Modifier.height(24.dp))

        // Username or Email TextField
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = {
                Text("Username or Email", color = Color.Gray, fontSize = 18.sp)
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.namebar),
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(28.dp)
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
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

        Spacer(modifier = Modifier.height(18.dp))

        // Password TextField
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
                val visibilityIcon =
                    if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility
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
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
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

        Spacer(modifier = Modifier.height(18.dp))

        // Description Text
        Text(
            text = "To delete your account, you must enter your account information.",
            color = Color.White,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Delete Button
        Button(
            onClick = { /* Delete account action */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF0000)), // Red button
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .height(56.dp)
        ) {
            Text(
                text = "Delete",
                color = Color.White,
                fontSize = 24.sp,
                fontFamily = balootamma,

            )
        }
    }
}

