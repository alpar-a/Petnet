package com.example.petnet

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GallerySelectionScreen(navController: NavHostController) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance()
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var description by remember { mutableStateOf("") }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedImageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Image Selection
        Box(
            modifier = Modifier
                .size(300.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable { imagePickerLauncher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (selectedImageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(selectedImageUri),
                    contentDescription = "Selected Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text("Tap to select an image", color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Description Input
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description Here...") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Publish Button
        Button(
            onClick = {
                if (selectedImageUri != null && description.isNotEmpty() && userId != null) {
                    uploadPost(
                        userId = userId,
                        imageUri = selectedImageUri.toString(),
                        description = description,
                        db = db,
                        storage = storage,
                        context = context,
                        onSuccess = {
                            navController.navigate("feed") // Navigate back to feed
                        }
                    )
                } else {
                    Toast.makeText(context, "Please select an image and write a description!", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedImageUri != null && description.isNotEmpty()
        ) {
            Text("Publish")
        }
    }
}

fun uploadPost(
    userId: String,
    imageUri: String,
    description: String,
    db: FirebaseFirestore,
    storage: FirebaseStorage,
    context: Context,
    onSuccess: () -> Unit
) {
    val storageRef = storage.reference.child("feed_images/${System.currentTimeMillis()}.jpg")
    val uri = Uri.parse(imageUri)

    // Fetch the user's profile details
    val profileRef = db.collection("profiles").document(userId)
    profileRef.get()
        .addOnSuccessListener { document ->
            if (document.exists()) {
                val username = document.getString("username") ?: "Unknown"
                val profilePictureUrl = document.getString("profilePictureUrl") ?: ""

                // Upload the image to Firebase Storage
                storageRef.putFile(uri)
                    .addOnSuccessListener {
                        // Get the download URL
                        storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                            // Create a post map
                            val post = hashMapOf(
                                "userId" to userId,
                                "username" to username,
                                "profilePictureUrl" to profilePictureUrl,
                                "description" to description,
                                "imageUrl" to downloadUrl.toString(),
                                "timestamp" to System.currentTimeMillis()
                            )

                            // Add the post to Firestore
                            db.collection("feed")
                                .add(post)
                                .addOnSuccessListener {
                                    // Update the user's post count
                                    profileRef.update("posts", FieldValue.increment(1)) // Increment the posts count by 1
                                        .addOnSuccessListener {
                                            println("Post count updated successfully!")
                                        }
                                        .addOnFailureListener { e ->
                                            println("Failed to update post count: ${e.message}")
                                        }

                                    Toast.makeText(context, "Post published successfully!", Toast.LENGTH_SHORT).show()
                                    onSuccess()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(context, "Failed to publish post: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Failed to upload image: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(context, "Profile not found!", Toast.LENGTH_SHORT).show()
            }
        }
        .addOnFailureListener { e ->
            Toast.makeText(context, "Failed to fetch profile: ${e.message}", Toast.LENGTH_SHORT).show()
        }
}






