package com.example.playerlistapp.data.remote.dto

data class ResultDto(
    val name: NameDto,
    val nat: String?,
    val email: String?,
    val picture: PictureDto
)

data class NameDto(
    val first: String,
    val last: String
)

data class PictureDto(
    val large: String,
    val medium: String,
    val thumbnail: String
)
