package com.example.petnet
/*
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                            AnnouncementBanner()
                            Spacer(modifier = Modifier.height(16.dp))
                            UpcomingEventsSection()
                            Spacer(modifier = Modifier.height(16.dp))
                            MultipleEventsBanner()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun EventsTopBar() {
    TopAppBar(
        title = {
            Text(
                text = "PetNet",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = { /* Search Action */ }) {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = "Search Icon"
                )
            }
        },
        actions = {
            IconButton(onClick = { /* Menu Action */ }) {
                Icon(
                    painter = painterResource(R.drawable.ic_menu),
                    contentDescription = "Menu Icon"
                )
            }
        }
    )
}

@Composable
fun EventsBottomBar() {
    NavigationBar {
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_home),
                    contentDescription = "Home Icon"
                )
            },
            label = { Text("Home") },
            selected = false,
            onClick = { /* Navigate to Home */ }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_calendar),
                    contentDescription = "Events Icon"
                )
            },
            label = { Text("Events") },
            selected = true,
            onClick = { /* Already on Events */ }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = "Add Photo Icon"
                )
            },
            label = { Text("Photo") },
            selected = false,
            onClick = { /* Navigate to Photo */ }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.sos),
                    contentDescription = "SOS Icon"
                )
            },
            label = { Text("S.O.S") },
            selected = false,
            onClick = { /* Navigate to SOS */ }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.profile),
                    contentDescription = "Profile Icon"
                )
            },
            label = { Text("Profile") },
            selected = false,
            onClick = { /* Navigate to Profile */ }
        )
    }
}

@Composable
fun AnnouncementBanner() {
    Surface(
        color = Color(0xFFE1F5FE),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Join the Free Give-Away!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "Share your photo with the #PetNet and win a Christmas outfit.",
                    fontSize = 14.sp
                )
            }
            Image(
                painter = painterResource(R.drawable.),
                contentDescription = "Christmas Dog",
                modifier = Modifier.size(64.dp)
            )
        }
    }
}

@Composable
fun UpcomingEventsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Upcoming Events",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = "See All",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            EventCard(
                title = "Yoga Lesson With Your Human",
                date = "5 JAN",
                location = "Caferağa Mahallesi, Kadıköy, IST",
                imageRes = R.drawable.yoga
            )
            EventCard(
                title = "Interactive Dog Park",
                date = "10 JUNE",
                location = "Yıldız Park, Istanbul",
                imageRes = R.drawable.park
            )
        }
    }
}

@Composable
fun EventCard(title: String, date: String, location: String, imageRes: Int) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .width(160.dp)
    ) {
        Column {
            Image(
                painter = painterResource(imageRes),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = date,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 14.sp
                )
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = location,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun MultipleEventsBanner() {
    Surface(
        color = Color(0xFFFFF9C4),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Join Multiple Events\nGet a free pet food!",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            Image(
                painter = painterResource(R.drawable.pet_food),
                contentDescription = "Pet Food",
                modifier = Modifier.size(64.dp)
            )
        }
    }
}
*/