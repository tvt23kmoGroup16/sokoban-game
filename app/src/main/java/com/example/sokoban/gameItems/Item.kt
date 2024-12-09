package com.example.sokoban.gameItems

data class Item(
    val id: Int,
    val name: String,
    val description: String,
    val type: ItemType,
    val effect: ItemEffect?,
    var usesLeft: Int
)


