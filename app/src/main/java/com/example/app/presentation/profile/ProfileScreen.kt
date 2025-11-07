package com.example.app.presentation.profile

import androidx.compose.ui.res.painterResource
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

        // ✅ Avatar
        val imagePainter =
            if (!profile.avatarUri.isNullOrEmpty())
                rememberAsyncImagePainter(profile.avatarUri)
            else androidx.compose.ui.res.painterResource(id = R.drawable.ic_profile_placeholder)

        Image(
            painter = painterResource(R.drawable.ic_profile_placeholder),
            contentDescription = "Default Avatar",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .clickable { onEditClick() }
        )

        Spacer(Modifier.height(24.dp))

        // ✅ Texts (Bilgiler)
        Text("Ad: ${profile.name}", style = MaterialTheme.typography.bodyLarge)
        Text("Pozisyon: ${profile.title}", style = MaterialTheme.typography.bodyLarge)
        Text("CV Link: ${profile.resumeUrl}", style = MaterialTheme.typography.bodyMedium)

        Spacer(Modifier.height(24.dp))

        // ✅ Rezume açma butonu
        Button(
            onClick = {
                profile.resumeUrl?.takeIf { it.isNotBlank() }?.let {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                    context.startActivity(intent)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Резюме")
        }

        Spacer(Modifier.height(16.dp))

        // ✅ Düzenleme butonu
        Button(
            onClick = onEditClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Profili Düzenle")
        }
    }
}
