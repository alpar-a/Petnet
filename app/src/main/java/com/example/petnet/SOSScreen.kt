package com.example.petnet

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.petnet.ui.theme.PetnetTheme

@OptIn(ExperimentalMaterial3Api::class)
class SOSScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PetnetTheme {
                val navController = rememberNavController()
                Scaffold(
                    topBar = { SOSTopBar() },
                    bottomBar = { SOSBottomBar(navController) },
                    content = { padding ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding)
                        ) {
                            SOS()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun SOS() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                val intent = Intent(context, FoundPetScreen::class.java)
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(containerColor = blu),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .width(265.dp)
                .height(63.dp)
        ) {
            Text(
                text = "I Lost My Pet",
                color = bl,
                fontFamily = balootamma,
                fontSize = 24.sp
            )
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(vertical = 44.dp),
            color = Color.Gray,
            thickness = 1.dp
        )

        Button(
            onClick = {
                val intent = Intent(context, LostPetScreen::class.java)
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(containerColor = blu),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .width(265.dp)
                .height(63.dp)
        ) {
            Text(
                text = "I Found a Lost Pet",
                color = bl,
                fontFamily = balootamma,
                fontSize = 24.sp
            )
            }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SOSTopBar() {
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
fun SOSBottomBar(navController: NavHostController) {
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
                    contentDescription = "sos",
                    tint = org
                )
            },
            label = { Text("S.O.S",
                color = org) },
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