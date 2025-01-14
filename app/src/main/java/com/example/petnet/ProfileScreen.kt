package com.example.petnet

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.petnet.ui.theme.PetnetTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
class ProfileScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PetnetTheme {
                val navController = rememberNavController()
                Scaffold(
                    topBar = { ProfileTopBar() },
                    bottomBar = { ProfileBottomBar(navController) },
                    content = { padding ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding)
                        ) {
                            Profile()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ProfilePostsGrid(postImageUrls: List<String>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(postImageUrls) { imageUrl ->
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = "User Post",
                modifier = Modifier
                    .padding(4.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopBar() {
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
fun Profile() {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val userId = auth.currentUser?.uid
    val storage = FirebaseStorage.getInstance()
    var profilePictureUrl by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("Username") }
    var additionalInfo by remember { mutableStateOf("Additional Info") }
    var postCount by remember { mutableStateOf(0) }
    var followersCount by remember { mutableStateOf(0) }
    var followingCount by remember { mutableStateOf(0) }
    var posts by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var isEditDialogOpen by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Fetch user data on launch
    LaunchedEffect(Unit) {
        if (userId != null) {
            db.collection("profiles").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        username = document.getString("username") ?: "Username"
                        additionalInfo = document.getString("additionalInfo") ?: "Additional Info"
                        profilePictureUrl = document.getString("profilePictureUrl") ?: ""
                        postCount = document.getLong("posts")?.toInt() ?: 0
                        followersCount = document.getLong("followers")?.toInt() ?: 0
                        followingCount = document.getLong("following")?.toInt() ?: 0
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Failed to fetch profile: ${e.message}", Toast.LENGTH_SHORT).show()
                }

            // Fetch posts for this user
            db.collection("feed")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener { documents ->
                    posts = documents.mapNotNull { it.data }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Failed to fetch posts: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        // Profile Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Inside Profile composable
            Box {
                // Image picker logic
                val imagePickerLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.GetContent()
                ) { uri ->
                    uri?.let {
                        val storageRef = storage.reference.child("profile_pictures/$userId.jpg")
                        storageRef.putFile(it)
                            .addOnSuccessListener {
                                storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                                    profilePictureUrl = downloadUrl.toString()
                                    db.collection("profiles").document(userId!!)
                                        .update("profilePictureUrl", profilePictureUrl)
                                        .addOnSuccessListener {
                                            Toast.makeText(context, "Profile picture updated!", Toast.LENGTH_SHORT).show()
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(context, "Failed to update Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
                                        }
                                }
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(context, "Failed to upload image: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                }

                Image(
                    painter = if (profilePictureUrl.isNotEmpty()) {
                        rememberAsyncImagePainter(profilePictureUrl)
                    } else {
                        painterResource(id = R.drawable.default_profile_pic)
                    },
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .clickable { imagePickerLauncher.launch("image/*") }, // Launch image picker
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.width(16.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(username, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(Modifier.weight(1f))
                    Button(
                        onClick = { isEditDialogOpen = true },
                        modifier = Modifier
                            .height(30.dp)
                            .width(100.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("EDIT", fontSize = 12.sp)
                    }
                }
                Text("@$username", color = Color.Gray)
                Text(additionalInfo, modifier = Modifier.padding(top = 5.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Stats Row
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            ProfileStat(count = postCount.toString(), label = "Posts")
            ProfileStat(count = followersCount.toString(), label = "Followers")
            ProfileStat(count = followingCount.toString(), label = "Following")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Posts Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(posts) { post ->
                val imageUrl = post["imageUrl"] as? String ?: ""
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = "Post Image",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }

        // Edit Profile Dialog
        if (isEditDialogOpen) {
            EditProfileDialog(
                currentUsername = username,
                currentInfo = additionalInfo,
                onSave = { newUsername, newInfo ->
                    coroutineScope.launch {
                        if (userId == null) {
                            Toast.makeText(context, "User ID is null. Cannot update profile.", Toast.LENGTH_SHORT).show()
                            return@launch
                        }
                        db.collection("profiles").document(userId)
                            .update(
                                mapOf(
                                    "username" to newUsername,
                                    "additionalInfo" to newInfo
                                )
                            )
                            .addOnSuccessListener {
                                username = newUsername
                                additionalInfo = newInfo
                                Toast.makeText(context, "Profile updated!", Toast.LENGTH_SHORT).show()
                                isEditDialogOpen = false
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(context, "Failed to update profile: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                },
                onClose = { isEditDialogOpen = false }
            )
        }
    }
}







@Composable
fun EditProfileDialog(
    currentUsername: String,
    currentInfo: String,
    onSave: (String, String) -> Unit,
    onClose: () -> Unit
) {
    var newUsername by remember { mutableStateOf(currentUsername) }
    var newInfo by remember { mutableStateOf(currentInfo) }

    AlertDialog(
        onDismissRequest = onClose,
        title = { Text("Edit Profile") },
        text = {
            Column {
                OutlinedTextField(
                    value = newUsername,
                    onValueChange = { newUsername = it },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = newInfo,
                    onValueChange = { newInfo = it },
                    label = { Text("Additional Info") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = { onSave(newUsername, newInfo) }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onClose) {
                Text("Cancel")
            }
        }
    )
}


@Composable
fun ProfileStat(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(count, fontWeight = FontWeight.Bold)
        Text(label, fontSize = 12.sp)
    }
}

@Composable
fun ProfileBottomBar(navController: NavHostController) {
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
                    contentDescription = "Profile",
                    tint = org
                )
            },
            label = { Text("Profile",
                color = org) },
            selected = false,
            onClick = {
                val intent = Intent(context, ProfileScreen::class.java)
                context.startActivity(intent)
            }
        )
    }
}