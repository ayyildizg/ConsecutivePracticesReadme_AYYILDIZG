package com.example.playerlistapp.data.model.repository

import com.example.playerlistapp.data.model.Player
import com.example.playerlistapp.data.remote.RandomUserApi
import com.example.playerlistapp.data.remote.mapper.toPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object PlayerRepository {

    private val api: RandomUserApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://randomuser.me/api/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(RandomUserApi::class.java)
    }

    suspend fun getPlayers(): List<Player> = withContext(Dispatchers.IO) {
        try {
            val response = api.getPlayers()
            response.results.map { it.toPlayer() }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
