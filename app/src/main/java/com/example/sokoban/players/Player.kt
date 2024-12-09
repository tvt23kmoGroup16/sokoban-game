package com.example.sokoban.players


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Toast
import java.io.IOException
import java.io.InputStream

open class Player(val context: Context, val layout: GridLayout, val level: Int) {

    var currentRow: Int = 0
    var currentColumn: Int = 0

    open fun getNewPositionForSpeedBoost(): Pair<Int, Int>? {
        val newRow = currentRow
        val newColumn = currentColumn

        if (newRow in 0..12&& newColumn in 0..12) {
            return Pair(newRow, newColumn)
        }
        return null
    }

    open fun movePlayerToNewPosition(newPosition: Pair<Int, Int>) {
        val newRow = newPosition.first
        val newColumn = newPosition.second

        // Assuming layout is a GridLayout and each tile is represented by an ImageView
        val newTile = layout.getChildAt(newRow * layout.columnCount + newColumn) as ImageView

        // Remove player from the old position
        val oldTile = layout.getChildAt(currentRow * layout.columnCount + currentColumn) as ImageView
        oldTile.setImageBitmap(null)

        // Place player on the new tile
        newTile.setImageBitmap(R.color.transparent)

        // Update the player's current position
        currentRow = newRow
        currentColumn = newColumn
    }

    // Activate Speed Boots effect
    open fun activateSpeedBoots() {
        Toast.makeText(context, "Speed Boots activated! You will move 2 tiles", Toast.LENGTH_SHORT)
            .show()
        val newPosition = getNewPositionForSpeedBoost()
        if (newPosition != null) {
            // Move player to the new position
            movePlayerToNewPosition(newPosition)
            Toast.makeText(context, "You are wearing Speed Boots! Moved 2 tiles.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Cannot move with Speed Boots!", Toast.LENGTH_SHORT).show()
        }
    }

    open fun useMagicWand() {
        // Find a random empty location
        val emptyLocation = findRandomEmptyTile()

        if (emptyLocation != null) {
            // Pass the level to the placeBox method
            placeBox(emptyLocation, level) // Using the level from the Player class
            Toast.makeText(context, "A new box has been created!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "No space available to create a new box!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun findRandomEmptyTile(): View? {
        val childCount = layout.childCount
        val randomIndex = (0 until childCount).random() // Getting random index
        val tile = layout.getChildAt(randomIndex)

        // Check if the tile is marked as "empty"
        if (tile.tag == " ") {
            return tile
        }
        return null
    }

    private fun placeBox(tile: View, level: Int) {
        val boxBitmap = getBoxImageForLevel(level)

        if (boxBitmap != null) {
            // Assuming each tile is a GridLayout child
            val imageView = tile.findViewById<ImageView>(android.R.id.icon>
            imageView?.setImageBitmap(android.R.id.icon)

        } else {
            Toast.makeText(context, "Error loading box image", Toast.LENGTH_SHORT).show()
        }

        tile.tag = "box" // Marking the tile occupied by the box
    }

    private fun getBoxImageForLevel(level: Int): Bitmap? {
        val assetManager = context.assets
        return try {
            val filename = when (level) {
                1 -> "jungleBoulder.png"
                2 -> "scifiBoulder.png"
                3 -> "pot_of_gold.png"
                4 -> "ancientBoulder.png"
                5 -> "westernBox.png"
                6 -> "horrorBox.png"
                else -> "jungleBoulder.png"
            }

            val inputStream: InputStream = assetManager.open(filename)
            BitmapFactory.decodeStream(inputStream)

        } catch (e: IOException) {
            e.printStackTrace()
            null  // Return null when error
        }
    }
}

enum class PlayerType(val displayName: String) {
    ALIEN("Alien"),
    GNOME("Gnome"),
    HUMAN("Human"),
    ALL("All")
}