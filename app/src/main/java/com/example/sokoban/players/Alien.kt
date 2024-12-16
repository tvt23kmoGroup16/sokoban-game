package com.example.sokoban.players

import android.content.Context
import android.widget.GridLayout
import android.widget.Toast
import com.example.sokoban.gameItems.Item
import com.example.sokoban.utils.TileUtils
import com.example.sokoban.utils.findNearestTileWithTag

class Alien(context: Context, layout: GridLayout, level: Int) : Player(context, layout, level, PlayerType.ALIEN) {

    // Override the useRayGun method for Alien's specific logic
    override fun useRayGun(item: Item, range: Int) {
        val playerPosition = getPlayerPosition()

        // Find the nearest boulder within the specified range
        val nearestBoxTile = findNearestTileWithTag(layout, playerPosition, "B", range)

        if (nearestBoxTile != null) {
            // Clear the boulder from the tile
            TileUtils.clearTile(nearestBoxTile)
            Toast.makeText(
                context,
                "Kaboom! The boulder exploded! You blast away with your laser gun!",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(context, "No boulder found nearby!", Toast.LENGTH_SHORT).show()
        }

        // Check win condition after using the Ray Gun
        checkWinCondition()
    }

    // Alien-specific implementation of speed boost activation
    override fun activateSpeedBoots(item: Item) {
        if (item.usesLeft > 0) {
            val newPosition = getNewPositionForSpeedBoost()
            if (newPosition != null) {
                movePlayerToNewPosition(newPosition)
                item.usesLeft--  // Decrease the remaining uses of Speed Boots
                Toast.makeText(
                    context,
                    "Alien's enormous feet get faster!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(context, "Cannot move to this position!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}



