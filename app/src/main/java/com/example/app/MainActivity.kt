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
import androidx.activity.result.contract.ActivityResultContracts
import android.os.Build


class MainActivity : ComponentActivity() {

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            // granted: true / false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }

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
