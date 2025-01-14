package com.example.petnet

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
                Scaffold(
                    topBar = { ProfileTopBar() },
                    bottomBar = { ProfileBottomBar() },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopBar() {
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
fun Profile() {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val userId = auth.currentUser?.uid
    val storage = FirebaseStorage.getInstance()

    var profilePictureUrl by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("Username") }
    var additionalInfo by remember { mutableStateOf("Additional Info") }
    var posts by remember { mutableStateOf(0) } // Initialize the posts variable

    var isEditDialogOpen by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Image picker launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                val storageRef = storage.reference.child("profile_pictures/$userId.jpg")
                storageRef.putFile(uri)
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
    )

    // Fetch user data on launch
    LaunchedEffect(Unit) {
        if (userId != null) {
            db.collection("profiles").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        username = document.getString("username") ?: "Username"
                        additionalInfo = document.getString("additionalInfo") ?: "Additional Info"
                        profilePictureUrl = document.getString("profilePictureUrl") ?: ""
                        posts = (document.getLong("posts") ?: 0).toInt() // Safely cast to Int
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Failed to fetch profile: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box {
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
                        .clickable { launcher.launch("image/*") },
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
                Text("Posts: $posts", modifier = Modifier.padding(top = 5.dp)) // Display post count
                Text(additionalInfo, modifier = Modifier.padding(top = 5.dp))
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
fun ProfileBottomBar() {
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
            selected = true,
            onClick = {
                val intent = Intent(context, ProfileScreen::class.java)
                context.startActivity(intent)
            }
        )
    }
}
