package com.example.app.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.app.domain.model.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("profile_prefs")

class ProfileDataStore(private val context: Context) {

    private object Keys {
        val NAME = stringPreferencesKey("name")
        val TITLE = stringPreferencesKey("title")
        val AVATAR_URI = stringPreferencesKey("avatar_uri")
        val RESUME_URL = stringPreferencesKey("resume_url")
    }

    suspend fun saveProfile(profile: Profile) {
        context.dataStore.edit { prefs ->
            prefs[Keys.NAME] = profile.name
            prefs[Keys.TITLE] = profile.title
            prefs[Keys.AVATAR_URI] = profile.avatarUri ?: ""
            prefs[Keys.RESUME_URL] = profile.resumeUrl
        }
    }

    fun getProfile(): Flow<Profile> =
        context.dataStore.data.map { prefs ->
            Profile(
                name = prefs[Keys.NAME] ?: "",
                title = prefs[Keys.TITLE] ?: "",
                avatarUri = prefs[Keys.AVATAR_URI],
                resumeUrl = prefs[Keys.RESUME_URL] ?: ""
            )
        }
}

