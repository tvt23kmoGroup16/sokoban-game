package com.example.sokoban.gameItems


data class ItemEffect(
    val movingSpeed: Int? = null, // Two tiles on one move
    val createBox: Boolean = false, // Creates a new box
    val smash: Boolean = false, // Destroys a box
    val explode: Boolean= false // Destroys a box
)
