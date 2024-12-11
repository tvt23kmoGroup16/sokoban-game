package com.example.sokoban.players

import android.content.Context
import android.widget.GridLayout
import android.widget.Toast
import com.example.sokoban.GameActivity
import com.example.sokoban.gameItems.Item
import com.example.sokoban.gameItems.ItemType
import com.example.sokoban.utils.TileUtils

class Alien(context: Context, layout: GridLayout, level: Int) : Player(context, layout, level, PlayerType.ALIEN) {
    override fun useItem(item: Item) {
        when (item.type) {
            ItemType.RAY_GUN -> destroyNearestBox(item, 3)
            ItemType.SPEED_BOOTS -> activateSpeedBoots(item)
            ItemType.MAGIC_WAND -> useMagicWand(item)
            else -> Toast.makeText(context, "You don't know how to use this!", Toast.LENGTH_SHORT).show()
        }
    }

override fun destroyNearestBox(item: Item, range: Int) {
        val playerPosition = getPlayerPosition()
        val nearestBoxTile = TileUtils.findNearestTileWithTag(layout, playerPosition, "box")

        if (nearestBoxTile != null) {
            TileUtils.clearTile(nearestBoxTile)
            Toast.makeText(
                context,
                "Kaboom! The boulder exploded! You blast away with your laser gun!",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(context, "No boulder found nearby!", Toast.LENGTH_SHORT).show()
        }
    }

    fun checkWinCondition() {
        val remainingBoxes = TileUtils.countRemainingBoxes(layout)
        if (remainingBoxes == 0) {
            (context as? GameActivity)?.showWinDialog()
        } else {
            Toast.makeText(context, "$remainingBoxes boulders remaining.", Toast.LENGTH_SHORT).show()
        }
    }

    // Gnome-specific activateSpeedBoots implementation
    override fun activateSpeedBoots(item: Item) {
        if (item.usesLeft > 0) {
            val newPosition = getNewPositionForSpeedBoost()
            if (newPosition != null) {
                movePlayerToNewPosition(newPosition)
                item.usesLeft--
                Toast.makeText(
                    context,
                    "Alien's enormous feet get faster!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(context, "Cannot move with Speed Boots!", Toast.LENGTH_SHORT).show()
            }
            if (item.usesLeft <= 0) {
                Toast.makeText(context, "Speed Boots are out of uses!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Speed Boots are out of uses!", Toast.LENGTH_SHORT).show()
        }
    }
}



