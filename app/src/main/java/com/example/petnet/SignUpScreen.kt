package com.example.petnet

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.withStyle
import com.example.petnet.ui.theme.PetnetTheme

val nunitosans = FontFamily(Font(R.font.nunitosans))

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

    Column(
        modifier = Modifier
            .fillMaxSize(),
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
            placeholder = {
                Text("Username or Email", color = gr,
                    fontSize = 13.sp,
                    fontFamily = nunito,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center)
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
            placeholder = {
                Text("Password", color = gr,
                    fontSize = 13.sp,
                    fontFamily = nunito,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center)
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
            placeholder = {
                Text("Confirm Password", color = gr,
                    fontSize = 13.sp,
                    fontFamily = nunito,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center)
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


        Text(
            text = buildAnnotatedString {
                append("By clicking the ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Register")
                }
                append(" button, you agree to the public offer")
            },
            color = Color.White,
            fontSize = 14.sp,
            fontFamily = nunitosans,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(vertical = 20.dp, horizontal = 51.dp),
            textAlign = TextAlign.Start
        )

        Button(
            onClick = {
                // Perform registration logic
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
