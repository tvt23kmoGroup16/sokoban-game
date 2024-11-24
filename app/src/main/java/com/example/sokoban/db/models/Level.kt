package com.example.sqlite_playground.db.models

data class Level(
    val id: Long,
    val levelNumber: Int,
    val levelName: String,
    val difficulty: String,
    val requiredScore: Int
)