package com.example.petnet

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petnet.ui.theme.PetnetTheme

class ForgotPasswordScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PetnetTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFEB6423)
                ) {
                    ForgotPassword()
                }
            }
        }
    }
}

@Composable
fun ForgotPassword() {
    var email by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = painterResource(id = R.drawable.paws3),
            contentDescription = "Paws Background",
            modifier = Modifier
                .width(100.dp)
                .height(200.dp),
            contentScale = ContentScale.FillWidth
        )

        Text(
            text = "Forgot",
            color = Color.White,
            fontSize = 32.sp,
            fontFamily = balootamma,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 8.dp)
        )
        Text(
            text = "password?",
            color = Color.White,
            fontSize = 32.sp,
            fontFamily = balootamma,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 30.dp, start = 8.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = {
                Text("Enter your email address", color = Color.Gray, fontSize = 18.sp)
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

        Text(
            text = "We will send you a message to set or reset your new password",
            color = Color.White,
            fontSize = 14.sp,
            fontFamily = nunitosans,
            modifier = Modifier
                .padding(top = 32.dp, bottom = 80.dp, start = 32.dp, end = 32.dp)
        )

        Button(
            onClick = {
                // Perform registration logic
            },
            colors = ButtonDefaults.buttonColors(containerColor = yellow),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp)
                .height(56.dp),
        ) {
            Text(
                text = "Send Code",
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
                .padding(top = 100.dp, bottom = 10.dp)
                .clickable {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                    (context as? ComponentActivity)?.finish()
                }
                .align(Alignment.CenterHorizontally)
        )

    }
}