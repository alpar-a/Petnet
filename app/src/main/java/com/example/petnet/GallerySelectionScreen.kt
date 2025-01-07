package com.example.petnet

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GallerySelectionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                GallerySelectionScreen(onImageSelected = { selectedImagePath ->
                    // Handle image selection here (e.g., navigate to the next screen or process the image)
                })
            }
        }
    }
}
@Composable
fun MainActivityContent() {
    RequestStoragePermission {
        GallerySelectionScreen(onImageSelected = { imageUri ->
            // Handle the selected image URI here
            println("Selected image URI: $imageUri")
        })
    }
}
@Composable
fun PermissionHandler(
    permission: String,
    onPermissionGranted: @Composable () -> Unit,
    onPermissionDenied: @Composable () -> Unit
) {
    val context = LocalContext.current
    val isPermissionGranted = remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            isPermissionGranted.value = isGranted
        }
    )

    if (isPermissionGranted.value) {
        onPermissionGranted()
    } else {
        LaunchedEffect(Unit) {
            permissionLauncher.launch(permission)
        }
        onPermissionDenied()
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GallerySelectionScreen(onImageSelected: (String) -> Unit = {}) {
    val context = LocalContext.current
    val permissionGranted = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionGranted.value = isGranted
        if (!isGranted) {
            Toast.makeText(
                context,
                "Storage permission is required to access gallery.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    LaunchedEffect(Unit) {
        if (!permissionGranted.value) {
            permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    if (permissionGranted.value) {
        val galleryImages = remember { mutableStateListOf<Uri>() }

        // Fetch images
        LaunchedEffect(Unit) {
            galleryImages.clear()
            galleryImages.addAll(fetchGalleryImages(context))
        }

        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Gallery Selection") })
            }
        ) { paddingValues ->
            if (galleryImages.isNotEmpty()) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    items(galleryImages) { imageUri ->
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onImageSelected(imageUri.toString()) }
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(imageUri),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(100.dp)
                                    .fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            } else {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text("No images found.")
                }
            }
        }
    } else {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text("Permission is required to access gallery.")
        }
    }
}



fun openAppSettings(activity: Activity) {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", activity.packageName, null)
    ).also(activity::startActivity)
}




@Composable
fun RequestStoragePermission(
    onPermissionGranted: @Composable () -> Unit
) {
    val context = LocalContext.current
    val permissionState = remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            permissionState.value = isGranted
            if (!isGranted) {
                Toast.makeText(context, "Storage permission is required.", Toast.LENGTH_SHORT).show()
            }
        }
    )

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            permissionState.value = true
        } else {
            permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    if (permissionState.value) {
        onPermissionGranted()
    }
}



fun fetchGalleryImages(context: Context): List<Uri> {
    val imageUris = mutableListOf<Uri>()
    val collection =
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

    val projection = arrayOf(MediaStore.Images.Media._ID)
    val query = context.contentResolver.query(
        collection,
        projection,
        null,
        null,
        "${MediaStore.Images.Media.DATE_ADDED} DESC"
    )

    query?.use { cursor ->
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val uri = ContentUris.withAppendedId(collection, id)
            imageUris.add(uri)
        }
    }
    return imageUris
}

