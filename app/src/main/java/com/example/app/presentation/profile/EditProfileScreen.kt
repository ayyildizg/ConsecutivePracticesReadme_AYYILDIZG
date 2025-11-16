package com.example.app.presentation.profile

import android.Manifest
import android.app.TimePickerDialog
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.example.app.R
import com.example.app.domain.model.Profile
import com.example.app.notification.schedulePairReminder
import java.io.File
import java.util.Calendar

@Composable
fun EditProfileScreen(
    viewModel: ProfileViewModel,
    onSave: () -> Unit
) {
    val profile by viewModel.profile.collectAsState()
    val context = LocalContext.current

    var name by remember { mutableStateOf(profile.name) }
    var title by remember { mutableStateOf(profile.title) }
    var resumeUrl by remember { mutableStateOf(profile.resumeUrl) }
    var avatarUri by remember { mutableStateOf(profile.avatarUri) }

    // Yeni alan: çift zamanı
    var favoriteTime by remember { mutableStateOf(profile.favoritePairTime) }
    var timeError by remember { mutableStateOf<String?>(null) }

    // Avatar kaynak seçimi için diyalog
    var showAvatarSourceDialog by remember { mutableStateOf(false) }
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    // ---- ZAMAN VALIDATION ----
    fun validateTime(input: String): String? {
        if (input.isBlank()) return "Поле не может быть пустым"

        val regex = Regex("^([01]\\d|2[0-3]):[0-5]\\d$")
        if (!regex.matches(input)) return "Формат времени должен быть HH:mm"

        return null
    }

    // ---- TIME PICKER ----
    val timePickerDialog = remember {
        val now = Calendar.getInstance()
        TimePickerDialog(
            context,
            { _, hour, minute ->
                favoriteTime = "%02d:%02d".format(hour, minute)
                timeError = validateTime(favoriteTime)
            },
            now.get(Calendar.HOUR_OF_DAY),
            now.get(Calendar.MINUTE),
            true
        )
    }

    // ---- İZİNLER ----
    val neededReadPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        Manifest.permission.READ_MEDIA_IMAGES
    else
        Manifest.permission.READ_EXTERNAL_STORAGE

    val permissionLauncherGallery = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* gerekirse handle edebilirsin */ }

    val permissionLauncherCamera = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* Kamera izni sonucu */ }

    // ---- GALERİ LAUNCHER ----
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            avatarUri = it.toString()
            val updated = profile.copy(
                name = name,
                title = title,
                avatarUri = avatarUri,
                resumeUrl = resumeUrl,
                favoritePairTime = favoriteTime
            )
            viewModel.updateProfile(updated)
        }
    }

    // ---- KAMERA LAUNCHER ----
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempCameraUri != null) {
            avatarUri = tempCameraUri.toString()
            val updated = profile.copy(
                name = name,
                title = title,
                avatarUri = avatarUri,
                resumeUrl = resumeUrl,
                favoritePairTime = favoriteTime
            )
            viewModel.updateProfile(updated)
        }
    }

    fun createImageUri(): Uri? {
        val imagesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (imagesDir != null) {
            val imageFile = File.createTempFile("avatar_", ".jpg", imagesDir)
            return FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                imageFile
            )
        }
        return null
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Profil Düzenleme", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))

        // ---- AVATAR ----
        Image(
            painter = if (!avatarUri.isNullOrEmpty())
                rememberAsyncImagePainter(avatarUri)
            else
                painterResource(R.drawable.ic_profile_placeholder),
            contentDescription = null,
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .clickable {
                    showAvatarSourceDialog = true
                }
        )

        Spacer(Modifier.height(16.dp))

        // ---- METİN ALANLARI ----
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Ad Soyad") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(10.dp))

        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Pozisyon / Unvan") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(10.dp))

        TextField(
            value = resumeUrl,
            onValueChange = { resumeUrl = it },
            label = { Text("CV / Resume URL") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(10.dp))

        // ---- ZAMAN ALANI ----
        OutlinedTextField(
            value = favoriteTime,
            onValueChange = { /* sadece time picker ile değişsin */ },
            label = { Text("Время любимой пары (HH:mm)") },
            trailingIcon = {
                Icon(
                    Icons.Filled.Schedule,
                    contentDescription = "Pick time",
                    modifier = Modifier.clickable { timePickerDialog.show() }
                )
            },
            readOnly = true,
            isError = timeError != null,
            modifier = Modifier.fillMaxWidth()
        )

        if (timeError != null) {
            Text(
                text = timeError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(Modifier.height(18.dp))

        // ---- KAYDET BUTONU ----
        Button(
            onClick = {
                timeError = validateTime(favoriteTime)

                if (timeError == null) {
                    val updated = profile.copy(
                        name = name,
                        title = title,
                        avatarUri = avatarUri,
                        resumeUrl = resumeUrl,
                        favoritePairTime = favoriteTime
                    )
                    viewModel.updateProfile(updated)
                    schedulePairReminder(context, favoriteTime)
                    onSave()
                }
            },
            enabled = timeError == null && favoriteTime.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Kaydet")
        }
    }

    // ---- AVATAR KAYNAĞI SEÇİM DİYALOĞU ----
    if (showAvatarSourceDialog) {
        AlertDialog(
            onDismissRequest = { showAvatarSourceDialog = false },
            title = { Text("Выбор источника") },
            text = { Text("Откуда взять фото?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showAvatarSourceDialog = false
                        // Kamera
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            permissionLauncherCamera.launch(Manifest.permission.CAMERA)
                        }
                        val uri = createImageUri()
                        if (uri != null) {
                            tempCameraUri = uri
                            cameraLauncher.launch(uri)
                        }
                    }
                ) {
                    Text("Камера")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showAvatarSourceDialog = false
                        // Galeri
                        permissionLauncherGallery.launch(neededReadPermission)
                        galleryLauncher.launch("image/*")
                    }
                ) {
                    Text("Галерея")
                }
            }
        )
    }
}
