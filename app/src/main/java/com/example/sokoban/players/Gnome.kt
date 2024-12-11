package com.example.sokoban.players

import android.content.Context
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Toast
import com.example.sokoban.GameActivity
import com.example.sokoban.gameItems.Item
import com.example.sokoban.gameItems.ItemType
import com.example.sokoban.utils.TileUtils
import com.example.sokoban.utils.findNearestTileWithTag

class Gnome(context: Context, layout: GridLayout, level: Int) : Player(context, layout, level, PlayerType.GNOME) {
    override fun useItem(item: Item) {
        when (item.type) {
            ItemType.MAGIC_CLUB -> destroyNearestBox(item, 2)
            ItemType.SPEED_BOOTS -> activateSpeedBoots(item)
            ItemType.MAGIC_WAND -> useMagicWand(item)
            else -> Toast.makeText(context, "You don't know how to use this!", Toast.LENGTH_SHORT)
                .show()
        }
    }

fun destroyNearestBox(item: Item, range: Int) {
    val playerPosition = getPlayerPosition()
    val nearestBoxTile = findNearestTileWithTag(layout, playerPosition, "B", 2)
    if (nearestBoxTile != null) {
        TileUtils.clearTile(nearestBoxTile)
        Toast.makeText(
            context,
            "Smashed! You swing your club and destroy the boulder!",
            Toast.LENGTH_SHORT
        ).show()
    } else {
        Toast.makeText(context, "No box found nearby!", Toast.LENGTH_SHORT).show()
    }
    checkWinCondition()

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
                        "Gnome's little feet are extra fast!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        "Cannot move to this position with Speed Boots!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

