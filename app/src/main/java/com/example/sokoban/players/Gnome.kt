package com.example.sokoban.players

import android.content.Context
import android.widget.GridLayout
import android.widget.Toast
import com.example.sokoban.gameItems.Item
import com.example.sokoban.gameItems.ItemType
import com.example.sokoban.utils.TileUtils
import com.example.sokoban.utils.findNearestTileWithTag



class Gnome(context: Context, layout: GridLayout, level: Int) : Player(context, layout, level, PlayerType.GNOME) {

    override fun useMagicClub(item: Item, range: Int) {
        val playerPosition = getPlayerPosition()
        val nearestBoxTile = findNearestTileWithTag(layout, playerPosition, "B", range)

        if (nearestBoxTile != null) {
            // Clear the boulder from the nearest box tile
            TileUtils.clearTile(nearestBoxTile)
            Toast.makeText(context, "Smashed! You swing your club and destroy the boulder!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "No box found nearby!", Toast.LENGTH_SHORT).show()
        }

        checkWinCondition()  // Check win condition after using the club
    }

    // Gnome-specific implementation of Speed Boots
    override fun activateSpeedBoots(item: Item) {
        if (item.usesLeft > 0) {
            val newPosition = getNewPositionForSpeedBoost()
            if (newPosition != null) {
                movePlayerToNewPosition(newPosition)
                item.usesLeft--  // Decrease the number of uses for the speed boots
                Toast.makeText(context, "Gnome's little feet are extra fast!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Cannot move to this position with Speed Boots!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
