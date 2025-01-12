package com.example.petnet

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petnet.ui.theme.PetnetTheme
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.storage
import com.google.firebase.storage.FirebaseStorage
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.layout.ContentScale
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.FirebaseFirestore
@OptIn(ExperimentalMaterial3Api::class)

class MainFeedScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        setContent {
            PetnetTheme {
                val navController = rememberNavController() // Initialize NavController
                Scaffold(
                    topBar = { TopBar() },
                    bottomBar = { BottomBar(navController) },
                    content = { padding ->
                        NavigationHost(padding)
                    }
                )
            }
        }
    }
}

@Composable
fun NavigationHost(padding: PaddingValues) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "feed" // Set your starting screen
    ) {
        composable("feed") { FeedContent(padding) }
        composable("gallerySelection") { GallerySelectionScreen() }
        // Add other screens as needed
    }
}

fun addFeedItem(
    username: String,
    caption: String,
    imageUrl: String,
    profilePictureUrl: String
) {
    val db = FirebaseFirestore.getInstance()
    val feedItem = hashMapOf(
        "username" to username,
        "caption" to caption,
        "imageUrl" to imageUrl,
        "profilePictureUrl" to profilePictureUrl
    )

    db.collection("feed")
        .add(feedItem)
        .addOnSuccessListener {
            println("Feed item added successfully!")
        }
        .addOnFailureListener { e ->
            println("Error adding feed item: ${e.message}")
        }
}

@Composable
fun FeedContent(padding: PaddingValues) {
    var imageUrls = listOf(
        "https://firebasestorage.googleapis.com/v0/b/petnet-a0517.firebasestorage.app/o/images%2FFeed-Cat%20(1).png?alt=media&token=e552b544-ad92-42a8-9226-a628eb1b08e9",
        "https://firebasestorage.googleapis.com/v0/b/petnet-a0517.firebasestorage.app/o/images%2FFeed-Cat.png?alt=media&token=5a67fd07-b98b-4c03-b5dc-87d4ae56f0bc",
        "https://firebasestorage.googleapis.com/v0/b/petnet-a0517.firebasestorage.app/o/images%2FFeed-Dog%20(1).png?alt=media&token=4081316f-069b-4808-800e-001360d26cfb",
        "https://firebasestorage.googleapis.com/v0/b/petnet-a0517.firebasestorage.app/o/images%2FFeed-Dog.png?alt=media&token=f803d3f8-8a1f-493a-bff1-f2582740b2ec"
    )


    // Fetch images from Firebase Storage
    LaunchedEffect(Unit) {
        fetchImagesFromFirebase { urls ->
            imageUrls = urls
            println("Fetched URLs: $urls") // Debugging log
        }
    }

    // Display images in a lazy column
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(imageUrls) { imageUrl ->
            FeedImageItem(imageUrl)
        }
    }
}



@Composable
fun FeedImageItem(imageUrl: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Crop
        )
    }
}



fun fetchImages(onResult: (List<String>) -> Unit) {
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference.child("images/")

    storageRef.listAll()
        .addOnSuccessListener { result ->
            val urls = mutableListOf<String>()
            val tasks = result.items.map { item ->
                item.downloadUrl.addOnSuccessListener { uri ->
                    urls.add(uri.toString())
                }
            }
            tasks.lastOrNull()?.addOnCompleteListener {
                onResult(urls)
            }
        }
        .addOnFailureListener {
            onResult(emptyList()) // Return empty list on failure
        }
}

fun fetchImagesFromFirebase(onResult: (List<String>) -> Unit) {
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference.child("images/") // Ensure this matches your Firebase Storage folder

    storageRef.listAll()
        .addOnSuccessListener { result ->
            val urls = mutableListOf<String>()
            result.items.forEach { item ->
                item.downloadUrl.addOnSuccessListener { uri ->
                    urls.add(uri.toString())
                    // Notify when all URLs are fetched
                    if (urls.size == result.items.size) {
                        onResult(urls)
                    }
                }
            }
        }
        .addOnFailureListener {
            onResult(emptyList())
        }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    val context = LocalContext.current
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
fun BottomBar(navController: NavController) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(
                context,
                "Storage permission is required to access photos.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
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
                if (ContextCompat.checkSelfPermission(
                        context,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    navController.navigate("gallerySelection")
                } else {
                    permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                }
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
