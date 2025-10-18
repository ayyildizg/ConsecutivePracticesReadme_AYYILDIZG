package com.example.playerlistapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playerlistapp.data.model.Player
import com.example.playerlistapp.data.model.repository.PlayerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class PlayerUiState {
    object Loading : PlayerUiState()
    data class Success(val players: List<Player>) : PlayerUiState()
    data class Error(val message: String) : PlayerUiState()
}

class PlayerViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<PlayerUiState>(PlayerUiState.Loading)
    val uiState: StateFlow<PlayerUiState> = _uiState

    init {
        fetchPlayers()
    }

    private fun fetchPlayers() {
        viewModelScope.launch {
            try {
                val players = PlayerRepository.getPlayers()
                _uiState.value = PlayerUiState.Success(players)
            } catch (e: Exception) {
                _uiState.value = PlayerUiState.Error("Ошибка загрузки: ${e.message}")
            }
        }
    }
}
