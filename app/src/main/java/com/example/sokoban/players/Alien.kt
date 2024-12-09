package com.example.sokoban.players

import android.content.Context
import android.widget.GridLayout
import android.widget.Toast
import com.example.sokoban.gameItems.ItemType
import com.example.sokoban.utils.TileUtils

class Alien(context: Context, layout: GridLayout, level: Int) : Player(context, layout, level) {

    // Track the number of boxes in the game
    private var remainingBoxes: Int = 0

    override fun useItem(item: ItemType) {
        when (item) {
            ItemType.RAY_GUN -> {
                destroyNearestBox()
                Toast.makeText(context, "You blast away with your gun!", Toast.LENGTH_SHORT).show()
            }
            ItemType.SPEED_BOOTS -> {
                activateSpeedBoots()
                Toast.makeText(context,"The boots fit your feet snugly.", Toast.LENGTH_SHORT).show()
            }
            ItemType.MAGIC_WAND -> {
                useMagicWand()
                Toast.makeText(context, "You wave the wand in your hand!", Toast.LENGTH_SHORT).show()
            }
            else -> Toast.makeText(context, "You cannot use this item!", Toast.LENGTH_SHORT).show()
        }
    }

    // Destroy the nearest box
    fun destroyNearestBox() {
        val playerPosition = getPlayerPosition()
        val nearestBoxTile = TileUtils.findNearestTileWithTag(layout, playerPosition, "box")

        if (nearestBoxTile != null) {
            TileUtils.clearTile(nearestBoxTile)
            Toast.makeText(context, "Boom! You exploded a boulder!", Toast.LENGTH_SHORT).show()

            // Decrease the number of remaining boxes
            remainingBoxes -= 1

            // If there are no boxes left, win the game
            if (remainingBoxes == 0) {
                winGame() // Call the win method
            }
        } else {
            Toast.makeText(context, "No boulders found nearby!", Toast.LENGTH_SHORT).show()
        }
    }

    // Activate the speed boots
    override fun activateSpeedBoots() {
        super.activateSpeedBoots()
        this.isSpeedBoosted = true
        this.speedBoostMovesRemaining = 3

        Toast.makeText(context, "These boots fit you snugly! You feel fast!", Toast.LENGTH_SHORT).show()
    }

    // Use the magic wand to create a new box
    override fun useMagicWand() {
        super.useMagicWand()
        this.useMagicWand = true
        this.MagicWandUsesRemaining = 3

        // Find an empty location to create a new box
        val emptyLocation = TileUtils.findRandomTileWithinRadius(layout, getPlayerPosition())
        if (emptyLocation != null) {
            // Place a box in the empty location
            TileUtils.clearTile(emptyLocation)  // Or use some method to place a box there
            remainingBoxes += 1  // Increment remaining boxes

            Toast.makeText(context, "You wave the magic wand and a new boulder appears!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "No space available to create a new boulder!", Toast.LENGTH_SHORT).show()
        }
    }

    fun setInitialBoxCount(count: Int) {
        remainingBoxes = count
    }

    // Method to handle winning the game
    private fun winGame() {
        Toast.makeText(context, "Congratulations! You won the game!", Toast.LENGTH_SHORT).show()
    }
}




