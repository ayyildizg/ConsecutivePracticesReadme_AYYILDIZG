package com.example.playerlistapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.playerlistapp.data.model.Player
import com.example.playerlistapp.data.model.repository.PlayerRepository

class PlayerViewModel : ViewModel() {
    private val repository = PlayerRepository()
    val players: List<Player> get() = repository.getPlayers()
}
