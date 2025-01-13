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
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.petnet.ui.theme.PetnetTheme

@OptIn(ExperimentalMaterial3Api::class)
class FoundPetScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PetnetTheme {
                Scaffold(
                    topBar = { FoundPetTopBar() },
                    bottomBar = { FoundPetBottomBar() },
                    content = { padding ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding)
                        ) {
                            FoundPet()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun FoundPet() {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Row with Back Icon
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { (context as? ComponentActivity)?.finish() }) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "Back",
                    modifier = Modifier.size(40.dp).padding(start = 10.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Information Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFA3D9FF), RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "We need some information\nabout your pet...",
                    fontSize = 19.sp,
                    color = bl,
                    fontFamily = nunito,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    painter = painterResource(id = R.drawable.info),
                    contentDescription = "Info",
                    tint = bl,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = {
                Text("Name: ",
                    color = bl,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = nunito)
            },
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = Color.Gray
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth(0.92f)
        )

        // Dropdown Menus
        Spacer(modifier = Modifier.height(30.dp))
        FoundCollarColor()
        Spacer(modifier = Modifier.height(30.dp))
        FoundBreed()
        Spacer(modifier = Modifier.height(30.dp))
        FoundGender()
        Spacer(modifier = Modifier.height(90.dp))
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Button(
                onClick = {

                },
                colors = ButtonDefaults.buttonColors(containerColor = blu),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(265.dp)
                    .height(63.dp)
            ) {
                Text(
                    text = "Pin Found Area",
                    color = bl,
                    fontFamily = balootamma,
                    fontSize = 20.sp
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoundCollarColor() {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Black", "Brown", "Gray", "White")
    val selectedOptions = remember { mutableStateListOf<String>() }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        // TextField displaying selected items
        OutlinedTextField(
            value = if (selectedOptions.isEmpty()) "Collar Color..." else selectedOptions.joinToString(", "),
            onValueChange = {},
            readOnly = true,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(0.92f),
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.dropdown),
                    contentDescription = "Dropdown",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { expanded = !expanded }
                )
            },
            placeholder = { Text("Collar Color...") }
        )

        // Dropdown menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = selectedOptions.contains(option),
                                onCheckedChange = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(option)
                        }
                    },
                    onClick = {
                        if (selectedOptions.contains(option)) {
                            selectedOptions.remove(option)
                        } else {
                            selectedOptions.add(option)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoundBreed() {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Beagle", "Bulldog", "German Shepherd", "Golden Retriever")
    var selectedOption by remember { mutableStateOf("") }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        // TextField displaying selected item
        OutlinedTextField(
            value = if (selectedOption.isEmpty()) "Breed..." else selectedOption,
            onValueChange = {},
            readOnly = true,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(0.92f),
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.dropdown),
                    contentDescription = "Dropdown",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { expanded = !expanded }
                )
            },
            placeholder = { Text("Breed...") }
        )

        // Dropdown menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        selectedOption = option
                        expanded = false
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoundGender() {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Male", "Female")
    var selectedOption by remember { mutableStateOf("") }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        // TextField displaying selected item
        OutlinedTextField(
            value = if (selectedOption.isEmpty()) "Gender..." else selectedOption,
            onValueChange = {},
            readOnly = true,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(0.92f),
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.dropdown),
                    contentDescription = "Dropdown",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { expanded = !expanded }
                )
            },
            placeholder = { Text("Gender...") }
        )

        // Dropdown menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        selectedOption = option
                        expanded = false
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoundPetTopBar() {
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
fun FoundPetBottomBar() {
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
            selected = true,
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