package com.example.sokoban.utils

import android.widget.Toast
import com.example.sokoban.gameItems.Item
import com.example.sokoban.players.Player
import com.example.sokoban.players.Alien
import com.example.sokoban.players.Gnome
import com.example.sokoban.gameItems.ItemType

class ItemUtils {

    // Method to activate Speed Boots
    fun useSpeedBoots(player: Player, item: Item) {
        if (player is Alien) {
            player.activateSpeedBoots(item)
        } else if (player is Gnome) {
            player.activateSpeedBoots(item)
        } else {
            Toast.makeText(player.context, "Speed Boots can't be used by this player.", Toast.LENGTH_SHORT).show()
        }
    }

    // Method to use Magic Wand
    fun useMagicWand(player: Player, item: Item) {
        if (player is Alien) {
            player.useMagicWand(item)
        } else if (player is Gnome) {
            player.useMagicWand(item)
        } else {
            Toast.makeText(player.context, "Magic Wand can't be used by this player.", Toast.LENGTH_SHORT).show()
        }
    }

    // Method to use Ray Gun
    fun useRayGun(player: Player, item: Item, range: Int) {
        if (player is Alien) {
            player.useRayGun(item, range)
        } else {
            Toast.makeText(player.context, "Ray Gun can't be used by this player.", Toast.LENGTH_SHORT).show()
        }
    }

    // Method to use Magic Club
    fun useMagicClub(player: Player, item: Item, range: Int) {
        if (player is Gnome) {
            player.useMagicClub(item, range)
        } else {
            Toast.makeText(player.context, "Magic Club can't be used by this player.", Toast.LENGTH_SHORT).show()
        }
    }

    // Additional helper methods if needed to interact with other items
    fun handleItemUse(player: Player, item: Item, range: Int = 0) {
        when (item.type) {
            ItemType.SPEED_BOOTS -> useSpeedBoots(player, item)
            ItemType.MAGIC_WAND -> useMagicWand(player, item)
            ItemType.RAY_GUN -> useRayGun(player, item, range)
            ItemType.MAGIC_CLUB -> useMagicClub(player, item, range)
            else -> {
                Toast.makeText(player.context, "Item can't be used by this player.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}