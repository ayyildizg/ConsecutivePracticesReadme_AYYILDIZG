package com.example.playerlistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.playerlistapp.ui.list.ListScreen
import com.example.playerlistapp.ui.theme.PlayerListAppTheme
import com.example.playerlistapp.ui.detail.DetailScreen
import com.example.playerlistapp.ui.settings.SettingsScreen
import com.example.playerlistapp.ui.profile.ProfileScreen
import com.example.playerlistapp.viewmodel.PlayerViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlayerListAppTheme {
                val navController = rememberNavController()
                val viewModel = PlayerViewModel()
                MainScreen(navController, viewModel)
            }
        }
    }
}

@Composable
fun MainScreen(navController: NavHostController, viewModel: PlayerViewModel) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("list", "profile", "settings")

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "List") },
                    label = { Text("List") },
                    selected = selectedItem == 0,
                    onClick = {
                        selectedItem = 0
                        navController.navigate("list") { popUpTo("list") { inclusive = true } }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    selected = selectedItem == 1,
                    onClick = {
                        selectedItem = 1
                        navController.navigate("profile") { popUpTo("list") }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") },
                    selected = selectedItem == 2,
                    onClick = {
                        selectedItem = 2
                        navController.navigate("settings") { popUpTo("list") }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "list",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("list") {
                ListScreen(viewModel = viewModel)
            }
            composable("profile") {
                ProfileScreen()
            }
            composable("settings") {
                SettingsScreen()
            }
            composable("detail/{name}/{number}/{position}/{imageRes}") { backStackEntry ->
                val name = backStackEntry.arguments?.getString("name") ?: ""
                val number = backStackEntry.arguments?.getString("number") ?: ""
                val position = backStackEntry.arguments?.getString("position") ?: ""
                val imageRes = backStackEntry.arguments?.getString("imageRes")?.toIntOrNull() ?: 0
                DetailScreen(name, number, position, imageRes)
            }
        }
    }
}
