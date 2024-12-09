package com.example.sokoban.gameItems

import android.content.Context
import android.widget.Toast
import com.example.sokoban.players.Player
import com.example.sokoban.players.Alien
import com.example.sokoban.players.Gnome

enum class ItemType {
    SPEED_BOOTS,
    MAGIC_WAND,
    RAY_GUN,
    MAGIC_CLUB
}

fun ItemType.performAction(player: Player, context: Context, item: Item) {
    when (this) {
        ItemType.SPEED_BOOTS -> {
            if (item.usesLeft > 0) {
                player.activateSpeedBoots()
                item.usesLeft -= 1
                Toast.makeText(context, "You feel faster with the Speed Boots!", Toast.LENGTH_SHORT).show()
                if (item.usesLeft == 0) {
                    Toast.makeText(context, "No uses left for the Speed Boots.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Speed Boots have no remaining uses!", Toast.LENGTH_SHORT).show()
            }
        }

        ItemType.MAGIC_WAND -> {
            if (item.usesLeft > 0) {
                player.useMagicWand()
                item.usesLeft -= 1
                Toast.makeText(context, "You used the Magic Wand and a boulder appears!", Toast.LENGTH_SHORT).show()
                if (item.usesLeft == 0) {
                    Toast.makeText(context, "No uses left for the Magic Wand.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "The Magic Wand has no uses left!", Toast.LENGTH_SHORT).show()
            }
        }

        ItemType.RAY_GUN -> {
            if (player is Alien) {
                player.destroyNearestBox()
                item.usesLeft -= 1
                Toast.makeText(context, "You used the Ray Gun!", Toast.LENGTH_SHORT).show()
                if (item.usesLeft == 0) {
                    Toast.makeText(context, "No uses left for the Ray Gun.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Only Aliens can use the Ray Gun!", Toast.LENGTH_SHORT).show()
            }
        }

        ItemType.MAGIC_CLUB -> {
            if (player is Gnome) {
                player.destroyNearestBox()
                item.usesLeft -= 1
                Toast.makeText(context, "You used the Magic Club!", Toast.LENGTH_SHORT).show()
                if (item.usesLeft == 0) {
                    Toast.makeText(context, "No uses left for the Magic Club.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Only Gnomes can use the Magic Club!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
