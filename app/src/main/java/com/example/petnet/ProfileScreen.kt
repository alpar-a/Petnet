package com.example.petnet

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
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

    // Declare variables for user data
    var username by remember { mutableStateOf("Veneta") }
    var additionalInfo by remember { mutableStateOf("Finnish Spitz, 1 years old // Female") }
    var profilePictureUrl by remember { mutableStateOf("") } // Declare profilePictureUrl variable
    var isEditDialogOpen by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Fetch user data from Firestore
    LaunchedEffect(Unit) {
        if (userId == null) {
            Toast.makeText(context, "User ID is null. Cannot load profile.", Toast.LENGTH_SHORT).show()
            return@LaunchedEffect
        }

        db.collection("profiles").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    username = document.getString("username") ?: "Unknown"
                    additionalInfo = document.getString("additionalInfo") ?: "No info available"
                    profilePictureUrl = document.getString("profilePictureUrl") ?: ""
                } else {
                    Toast.makeText(context, "Document does not exist", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Failed to load profile: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }


    Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = if (profilePictureUrl.isNotEmpty()) {
                    rememberAsyncImagePainter(profilePictureUrl) // Load from URL
                } else {
                    painterResource(id = R.drawable.default_profile_pic) // Default image
                },
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(16.dp))
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween // Even spacing
                ) {
                    Text(username, fontWeight = FontWeight.Bold, fontSize = 20.sp)

                    Button(
                        onClick = { isEditDialogOpen = true },
                        modifier = Modifier
                            .height(30.dp)
                            .widthIn(min = 100.dp), // Minimum width for consistency
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("EDIT PROFILE", fontSize = 12.sp)
                    }
                }
                Text("@$username", color = Color.Gray)
                Text(additionalInfo, modifier = Modifier.padding(top = 5.dp))
            }

        }

        Row(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ProfileStat("62", "Posts")
            ProfileStat("1852", "Followers")
            ProfileStat("2100", "Following")
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
                            .set(
                                mapOf(
                                    "username" to newUsername,
                                    "additionalInfo" to newInfo,
                                    "profilePictureUrl" to profilePictureUrl // Ensure this is included
                                ), SetOptions.merge()
                            )
                            .addOnSuccessListener {
                                username = newUsername
                                additionalInfo = newInfo
                                Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                                isEditDialogOpen = false
                            }
                            .addOnFailureListener { exception ->
                                Toast.makeText(context, "Failed to update profile: ${exception.message}", Toast.LENGTH_SHORT).show()
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
