package com.example.sokoban.gameItems

import com.google.android.gms.games.Player

enum class ItemType {
        RAY_GUN,
        MAGIC_CLUB,
        SPEED_BOOTS,
        MAGIC_WAND,
        NONE
    }

    class Item(val type: ItemType, val description: String, val effect: (Player) -> Unit)

}
