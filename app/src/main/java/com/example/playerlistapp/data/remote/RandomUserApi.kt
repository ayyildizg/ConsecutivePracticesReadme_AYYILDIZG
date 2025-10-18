package com.example.playerlistapp.data.remote

import com.example.playerlistapp.data.remote.dto.RandomUserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomUserApi {
    @GET("/")
    suspend fun getPlayers(
        @Query("results") results: Int = 20,
        @Query("page") page: Int = 1,
        @Query("seed") seed: String = "urfuu"
    ): RandomUserResponse
}
