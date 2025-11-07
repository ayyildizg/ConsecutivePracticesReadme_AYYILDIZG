package com.example.app.presentation.profile

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.app.R
import com.example.app.domain.model.Profile

@Composable
fun EditProfileScreen(
    viewModel: ProfileViewModel,
    onSave: () -> Unit
) {
    val profile by viewModel.profile.collectAsState()

    var name by remember { mutableStateOf(profile.name) }
    var title by remember { mutableStateOf(profile.title) }
    var resumeUrl by remember { mutableStateOf(profile.resumeUrl) }
    var avatarUri by remember { mutableStateOf(profile.avatarUri) }

    // ✅ Sadece galeri izni
    val neededReadPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        Manifest.permission.READ_MEDIA_IMAGES
    else
        Manifest.permission.READ_EXTERNAL_STORAGE

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    // ✅ Galeri seçici
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            avatarUri = it.toString()
            viewModel.updateProfile(Profile(name, title, avatarUri, resumeUrl)) // anında güncelle
        }
    }

    Column(
        Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Profil Düzenleme", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))

        // ✅ Foto gösterimi + tıklama
        Image(
            painter = if (!avatarUri.isNullOrEmpty())
                rememberAsyncImagePainter(avatarUri)
            else painterResource(R.drawable.ic_profile_placeholder),
            contentDescription = null,
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .clickable {
                    permissionLauncher.launch(neededReadPermission)
                    galleryLauncher.launch("image/*")
                }
        )

        Spacer(Modifier.height(16.dp))

        TextField(value = name, onValueChange = { name = it }, label = { Text("Ad Soyad") })
        Spacer(Modifier.height(10.dp))
        TextField(value = title, onValueChange = { title = it }, label = { Text("Pozisyon / Unvan") })
        Spacer(Modifier.height(10.dp))
        TextField(value = resumeUrl, onValueChange = { resumeUrl = it }, label = { Text("CV / Resume URL") })
        Spacer(Modifier.height(18.dp))

        Button(
            onClick = {
                viewModel.updateProfile(Profile(name, title, avatarUri, resumeUrl))
                onSave()
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Kaydet") }
    }
}
