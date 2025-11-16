package com.example.app.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.data.ProfileDataStore
import com.example.app.domain.model.Profile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val dataStore: ProfileDataStore
) : ViewModel() {

    private val _profile = MutableStateFlow(Profile())
    val profile: StateFlow<Profile> = _profile

    init {
        viewModelScope.launch {
            dataStore.getProfile().collect { saved ->
                _profile.value = saved
            }
        }
    }

    fun updateProfile(newProfile: Profile) {
        _profile.value = newProfile
        viewModelScope.launch {
            dataStore.saveProfile(newProfile)
        }
    }

    fun updateAvatar(uri: String) {
        val updated = _profile.value.copy(avatarUri = uri)
        updateProfile(updated)
    }

    fun updateFavoriteTime(time: String) {
        val updated = _profile.value.copy(favoritePairTime = time)
        updateProfile(updated)
    }
}


