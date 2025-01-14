package com.example.petnet

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.material.TabRowDefaults.Divider
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.example.petnet.ui.theme.PetnetTheme

@OptIn(ExperimentalMaterial3Api::class)
class EventsScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PetnetTheme {
                Scaffold(
                    topBar = { EventsTopBar() },
                    bottomBar = { EventsBottomBar() },
                    content = { padding ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding)
                        ) {
                            Events()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun Events() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        BoxWithConstraints {
            val image = painterResource(id = R.drawable.eventstext1)
            val screenWidth = maxWidth
            val aspectRatio = image.intrinsicSize.width / image.intrinsicSize.height

            Box(
                modifier = Modifier
                    .width(screenWidth)
                    .height(screenWidth / aspectRatio)
            ) {
                // Background Image
                Image(
                    painter = image,
                    contentDescription = "Background Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Overlay Text (Top Information)
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopStart)
                ) {
                    Text(
                        text = "Join the Free Give-Away!",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 22.sp,
                        fontFamily = balootamma,
                        color = bl,
                        modifier = Modifier
                            .padding(bottom = 0.dp)
                    )
                    Text(
                        text = buildAnnotatedString {
                            append("Share your photo with the\n")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("#PetNet")
                            }
                            append(" and win a\nChristmas outfit.")
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 16.sp,
                        fontFamily = nunito,
                        color = bl,
                        modifier = Modifier
                    )
                }
            }

        }
        Text(
            text = "Upcoming Events",
            style = MaterialTheme.typography.titleLarge,
            fontFamily = balootamma,
            color = bl,
            modifier = Modifier.padding(top = 8.dp)
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            items(listOf(
                Pair("Yoga Lesson With Your Human", R.drawable.event1),
                Pair("Interactive Dog Training", R.drawable.event2)
            )) { (event, image) ->
                EventCard(
                    eventTitle = event,
                    eventDate = "5 JAN",
                    eventLocation = "Caferağa Mahallesi, Kadıköy, IST",
                    eventImage = image
                )
            }
        }
        BoxWithConstraints {
            val image = painterResource(id = R.drawable.eventstext2)
            val screenWidth = maxWidth
            val aspectRatio = image.intrinsicSize.width / image.intrinsicSize.height

            Box(
                modifier = Modifier
                    .width(screenWidth)
                    .height(screenWidth / aspectRatio)
            ) {
                // Background Image
                Image(
                    painter = image,
                    contentDescription = "Background Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Overlay Text (Top Information)
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopStart)
                ) {
                    Text(
                        text = "Join Multiple Events",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 22.sp,
                        fontFamily = balootamma,
                        color = bl,
                        modifier = Modifier
                            .padding(bottom = 20.dp)
                    )
                    Text(
                        text = "Get a free pet food!",
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 16.sp,
                        fontFamily = nunito,
                        color = bl,
                        modifier = Modifier
                    )
                }
            }

        }
    }
}


@Composable
fun EventCard(eventTitle: String, eventDate: String, eventLocation: String, eventImage: Int) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .border(1.dp, Color.Transparent, MaterialTheme.shapes.medium)
            .background(red, MaterialTheme.shapes.medium)
            .padding(16.dp)
            .size(width = 237.dp, height = 255.dp)
    ) {
        Image(
            painter = painterResource(id = eventImage),
            contentDescription = eventTitle,
            modifier = Modifier
                .fillMaxWidth()
                .height(163.dp)
                .clip(MaterialTheme.shapes.medium)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = eventDate,
            style = MaterialTheme.typography.bodySmall,
            fontFamily = balootamma,
            color = org,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = eventTitle,
            style = MaterialTheme.typography.bodyMedium,
            fontFamily = balootamma,
            color = bl,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.location),
                contentDescription = "Location Icon",
                tint = bl,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = eventLocation,
                style = MaterialTheme.typography.bodySmall,
                fontFamily = nunito,
                color = bl,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsTopBar() {
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
fun EventsBottomBar() {
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
            selected = false,
            onClick = {
                val intent = Intent(context, MainFeedScreen::class.java)
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
            selected = true,
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