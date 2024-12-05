package com.example.sokoban.db.models

data class Settings(
    val id: Long,
    val userId: Long,
    val musicEnabled: Boolean,
    val soundEnabled: Boolean,
    val masterVolume: Int,
    val musicVolume: Int,
    val soundVolume: Int
)

