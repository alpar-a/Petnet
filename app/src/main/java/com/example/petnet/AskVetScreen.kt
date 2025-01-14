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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController


@OptIn(ExperimentalMaterial3Api::class)
class AskVetScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PetnetTheme {
                val navController = rememberNavController()
                Scaffold(
                    topBar = { AskVetTopBar() },
                    bottomBar = { AskVetBottomBar(navController) },
                    content = { padding ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding)
                        ) {
                            AskVet()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun AskVet() {
    val context = LocalContext.current
    var questionText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            IconButton(onClick = { (context as? ComponentActivity)?.finish() }) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.chat),
                contentDescription = "Question Icon",
                tint = org,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Ask a question to the vet",
                fontSize = 16.sp,
                fontFamily = nunito,
                fontWeight = FontWeight.Bold,
                color = org
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = questionText,
            onValueChange = { questionText = it },
            label = {
                Text("Ask question here :",
                    color = gr,
                    fontFamily = nunito,
                    fontWeight = FontWeight.Bold)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = wh,
                unfocusedContainerColor = wh,
                focusedBorderColor = gr.copy(alpha = 0.5f),
                unfocusedBorderColor = gr.copy(alpha = 0.5f),
                cursorColor = bl
            )
        )


        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Button(
                onClick = {
                    val intent = Intent(context, MainFeedScreen::class.java)
                    context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(containerColor = org),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(147.dp)
                    .height(55.dp)
            ) {
                Text(
                    text = "Create",
                    color = wh,
                    fontFamily = balootamma,
                    fontSize = 24.sp
                )
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AskVetTopBar() {
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
fun AskVetBottomBar(navController: NavHostController) {
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
            onClick = { navController.navigate("feed") }
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