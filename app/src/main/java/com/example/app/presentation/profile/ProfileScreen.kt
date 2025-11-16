package com.example.app.presentation.profile

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource      // ✅ DOĞRU import (bunu kullan!)
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.app.R

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onEditClick: () -> Unit
) {
    val profile = viewModel.profile.collectAsState().value
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(20.dp))

        // 🟣 Avatar painter
        val imagePainter =
            if (!profile.avatarUri.isNullOrEmpty())
                rememberAsyncImagePainter(profile.avatarUri)
            else painterResource(R.drawable.ic_profile_placeholder)   // ✅ DOĞRU ÇÖZÜM

        // 🟣 Avatar Image
        Image(
            painter = imagePainter,
            contentDescription = "Avatar",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .clickable { onEditClick() }
        )

        Spacer(Modifier.height(24.dp))

        Text("Ad: ${profile.name}", style = MaterialTheme.typography.bodyLarge)
        Text("Pozisyon: ${profile.title}", style = MaterialTheme.typography.bodyLarge)
        Text("CV Link: ${profile.resumeUrl}", style = MaterialTheme.typography.bodyMedium)

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                if (profile.resumeUrl.isNotBlank()) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(profile.resumeUrl))
                    context.startActivity(intent)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Резюме")
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onEditClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Profili Düzenle")
        }
    }
}
