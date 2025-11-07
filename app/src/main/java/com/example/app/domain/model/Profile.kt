package com.example.app.domain.model

data class Profile(
    val name: String = "",
    val title: String = "",
    val avatarUri: String? = null,
    val resumeUrl: String = ""
)

