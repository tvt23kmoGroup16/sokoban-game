package com.example.sokoban.db.models

data class User(
    val id: Long,
    val username: String,
    val email: String,
    val passwordHash: String,
    val lastLogin: Long,
    val score: Int,
    val level: Int
)