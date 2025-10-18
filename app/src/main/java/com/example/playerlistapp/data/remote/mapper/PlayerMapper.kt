package com.example.playerlistapp.data.remote.mapper

import com.example.playerlistapp.data.model.Player
import com.example.playerlistapp.data.remote.dto.ResultDto

fun ResultDto.toPlayer(): Player {
    return Player(
        name = "${name.first} ${name.last}",
        number = (1..99).random(),
        position = nat ?: "Unknown",
        imageRes = picture.large // URL olarak alıyoruz artık
    )
}
