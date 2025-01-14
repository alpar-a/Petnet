package com.example.petnet

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.petnet.ui.theme.PetnetTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.OutlinedTextField
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController


@OptIn(ExperimentalMaterial3Api::class)
class ContactScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PetnetTheme {
                val navController = rememberNavController()
                Scaffold(
                    topBar = { ContactTopBar() },
                    bottomBar = { ContactBottomBar(navController) },
                    content = { padding ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding)
                        ) {
                            Contact()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun Contact() {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { (context as? ComponentActivity)?.finish() }) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "Back",
                    modifier = Modifier.size(30.dp).padding(start = 10.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }

        Text(
            text = "Contact us",
            fontSize = 16.sp,
            color = Color(0xFFEB6423),
            fontWeight = FontWeight.Bold,
            fontFamily = nunito,
            modifier = Modifier.padding(start = 20.dp)
        )

        // Email and Phone
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "Email",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFFEB6423),
                    fontFamily = nunito,)
                Text(text = "petnet@mail.com")
            }
            Divider(
                modifier = Modifier
                    .height(37.dp)
                    .width(1.dp)
                    .background(Color(0xFF575757))
                    .padding(horizontal = 8.dp)
            )
            Column {
                Text(text = "Phone Number",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFEB6423),
                    fontSize = 14.sp,
                    fontFamily = nunito,)
                Text(text = "+90 500 000 00 00")
            }
        }

        // Name Input
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = { Text("Name",
                color = bl,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = nunito) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.Gray
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(64.dp)
        )

        // Email Input
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Email",
                color = bl,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = nunito) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.Gray
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(64.dp)
        )

        // Code and Phone Number Inputs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                value = code,
                onValueChange = { code = it },
                placeholder = { Text("Code",
                    color = bl,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = nunito) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 4.dp)
                    .height(64.dp)
            )

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                placeholder = { Text("Phone Number",
                    color = bl,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = nunito) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(2f)
                    .padding(start = 4.dp)
                    .height(64.dp)
            )
        }

        // Message Input
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Message...",
                color = bl,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = nunito) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(bottom = 16.dp)
        )

        // Submit Button
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Button(
                onClick = {
                    val intent = Intent(context, MainFeedScreen::class.java)
                    context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(containerColor = yellow),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(200.dp)
                    .height(56.dp)
            ) {
                Text(
                    text = "Submit",
                    color = Color(0xFFEB6423),
                    fontFamily = balootamma,
                    fontSize = 24.sp
                )
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactTopBar() {
    val context = LocalContext.current
    TopAppBar(
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.topbarlogo),
                    contentDescription = "PetNet Logo",
                    modifier = Modifier.size(120.dp)
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = { /* Search action */ }) {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = "Search"
                )
            }
        },
        actions = {
            IconButton(onClick = {
                val intent = Intent(context, MenuScreen::class.java)
                context.startActivity(intent)
            }) {
                Icon(
                    painter = painterResource(R.drawable.ic_menu),
                    contentDescription = "Menu"
                )
            }
        }
    )
}

@Composable
fun ContactBottomBar(navController: NavHostController) {
    val context = LocalContext.current
    var showPermissionsDialog by remember { mutableStateOf(false) }

    if (showPermissionsDialog) {
        HandlePermissions(
            onPermissionsGranted = {
                showPermissionsDialog = false
                navController.navigate("gallerySelection")
            },
            onPermissionsDenied = {
                showPermissionsDialog = false
                Toast.makeText(
                    context,
                    "Storage permissions are required to access photos.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }

    NavigationBar(
        modifier = Modifier
            .background(wh)
            .border(1.dp, gr.copy(alpha = 0.2f)),
        containerColor = wh
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_home),
                    contentDescription = "Home"
                )
            },
            label = { Text(
                "Home"
            )  },
            selected = false,
            onClick = { val intent = Intent(context, MainFeedScreen::class.java)
                context.startActivity(intent) }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_calendar),
                    contentDescription = "Events"
                )
            },
            label = { Text("Events") },
            selected = false,
            onClick = {
                val intent = Intent(context, EventsScreen::class.java)
                context.startActivity(intent)
            }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = "Photo"
                )
            },
            label = { Text("Photo") },
            selected = navController.currentDestination?.route == "gallerySelection",
            onClick = {
                showPermissionsDialog = true
            }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.sos),
                    contentDescription = "sos"
                )
            },
            label = { Text("S.O.S") },
            selected = false,
            onClick = {
                val intent = Intent(context, SOSScreen::class.java)
                context.startActivity(intent)
            }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.profile),
                    contentDescription = "Profile"
                )
            },
            label = { Text("Profile") },
            selected = false,
            onClick = {
                val intent = Intent(context, ProfileScreen::class.java)
                context.startActivity(intent)
            }
        )
    }
}