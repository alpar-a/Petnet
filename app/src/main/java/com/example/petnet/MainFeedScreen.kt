package com.example.petnet

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petnet.ui.theme.PetnetTheme

@OptIn(ExperimentalMaterial3Api::class)
class MainFeedScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PetnetTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "PetNet",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp
                                )
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
                                IconButton(onClick = { /* Menu action */ }) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_menu),
                                        contentDescription = "Menu"
                                    )
                                }
                            }
                        )
                    },
                    bottomBar = { BottomBar() }, // Fixed BottomBar
                    content = { padding ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding)
                        ) {
                            FeedItem(
                                username = "@boncuk_gozluu",
                                caption = "Biraz da burada yatayım dedim... #miskinlik #uykumodu",
                                imageRes = R.drawable.cat_image
                            )

                            FeedItem(
                                username = "@alex10",
                                caption = "Exploring the world!",
                                imageRes = R.drawable.dog_image
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun FeedItem(username: String, caption: String, imageRes: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.profile_placeholder),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = username, fontWeight = FontWeight.Bold)
        }
        Image(
            painter = painterResource(imageRes),
            contentDescription = "Post Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        Text(
            text = caption,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun BottomBar() {
    val context = LocalContext.current
    NavigationBar {
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_home),
                    contentDescription = "Home"
                )
            },
            label = { Text("Home") },
            selected = true,
            onClick = {
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            }
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
            selected = false,
            onClick = {

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
