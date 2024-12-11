package com.example.sokoban.gameItems

data class Item(
    val name: String,
    val type: ItemType,
    val description: String,
    val id: Int,
    var usesLeft: Int

)


