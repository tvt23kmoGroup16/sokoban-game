package com.example.sokoban.gameItems

data class Item(
    val id: Int,
    val name: String,
    val type: ItemType,
    val description: String,
    val effect: ItemEffect?,
    var usesLeft: Int
)


