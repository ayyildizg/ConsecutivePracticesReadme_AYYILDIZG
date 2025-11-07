package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.app.data.ProfileDataStore
import com.example.app.presentation.profile.EditProfileScreen
import com.example.app.presentation.profile.ProfileScreen
import com.example.app.presentation.profile.ProfileViewModel
import com.example.app.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { AppNavigation() }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val viewModel = remember { ProfileViewModel(ProfileDataStore(context)) }

    NavHost(navController = navController, startDestination = "profile") {
        composable("profile") {
            ProfileScreen(viewModel) { navController.navigate("edit") }
        }
        composable("edit") {
            EditProfileScreen(viewModel) { navController.popBackStack() }
        }
    }
}
