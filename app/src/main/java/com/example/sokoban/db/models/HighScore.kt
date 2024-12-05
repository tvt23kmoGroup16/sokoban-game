package com.example.sokoban.db.models

data class HighScore(
    val id: Long,
    val userId: Long,
    val score: Int,
    val level: Int,
    val date: String // String in "YYYY-MM-DD" format or ISO-8601
)
