package com.example.playerlistapp.data.model.repository

import com.example.playerlistapp.R
import com.example.playerlistapp.data.model.Player

class PlayerRepository {
    fun getPlayers(): List<Player> {
        return listOf(
            Player("Lionel Messi", "10", "Forward", R.drawable.messi),
            Player("Cristiano Ronaldo", "7", "Forward", R.drawable.ronaldo),
            Player("Kevin De Bruyne", "17", "Midfielder", R.drawable.debruyne),
            Player("Manuel Neuer", "1", "Goalkeeper", R.drawable.neuer),
            Player("Virgil van Dijk", "4", "Defender", R.drawable.vandijk)
        )
    }
}
