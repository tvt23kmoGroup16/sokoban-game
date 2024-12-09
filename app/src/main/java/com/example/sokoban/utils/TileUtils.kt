package com.example.sokoban.utils

import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import kotlin.math.absoluteValue

object TileUtils {

    /**
     * Finds the nearest box tile within the specified radius around the player.
     * @param layout GridLayout where tiles are located.
     * @param playerPosition Current position of the player (row, col).
     * @param radius Radius within which to look for a box tile.
     * @return The nearest box tile or null if no box is found.
     */

    fun findNearestBoxWithinRadius(
        layout: GridLayout,
        playerPosition: Pair<Int, Int>,
        radius: Int
    ): View? {
        val rows = layout.rowCount
        val cols = layout.columnCount

        // Player is within bounds
        for (r in -radius..radius) {
            for (c in -radius..radius) {
                val newRow = playerPosition.first + r
                val newCol = playerPosition.second + c

                if (newRow in 0 until rows && newCol in 0 until cols) {
                    val tile = layout.getChildAt(newRow * cols + newCol)
                    if (tile.tag == "box") {
                        return tile
                    }
                }
            }
        }

        return null // No box tile found within specified radius
    }
    /**
     * Clears a tile by resetting its tag and image resource.
     * @param tile The tile to clear.
     */
    fun clearTile(tile: View) {
        tile.tag = " "
        val imageView = tile.findViewById<ImageView>(android.R.id.icon)
        imageView?.setImageBitmap(null) // Clear image resource
    }
}

    /**
     * Finds a random empty tile within the specified radius around the player.
     */
    fun findRandomTileWithinRadius(
        layout: GridLayout,
        playerPosition: Pair<Int, Int>
    ): View? {
        val rows = layout.rowCount
        val cols = layout.columnCount
        val maxRadius = maxOf(rows, cols) // Maximum radius

        for (radius in 1..maxRadius) {
            val emptyTiles = findEmptyTilesInRadius(layout, playerPosition, radius)
            if (emptyTiles.isNotEmpty()) {
                return emptyTiles.random() // Randomly chooses one empty tile
            }
        }

        return null // No empty tile found
    }

    /**
     * Finds all tiles in a specific radius around the player's position.
     */
    private fun findTilesInRadius(
        layout: GridLayout,
        playerPosition: Pair<Int, Int>,
        radius: Int
    ): List<View> {
        val tilesInRadius = mutableListOf<View>()
        val rows = layout.rowCount
        val cols = layout.columnCount

        for (dx in -radius..radius) {
            for (dy in -radius..radius) {
                if (dx.absoluteValue + dy.absoluteValue == radius) { // Points exactly on the radius
                    val newRow = playerPosition.first + dx
                    val newCol = playerPosition.second + dy

                    if (newRow in 0 until rows && newCol in 0 until cols) {
                        val tile = layout.getChildAt(newRow * cols + newCol)
                        tilesInRadius.add(tile)
                    }
                }
            }
        }

        return tilesInRadius
    }

    /**
     * Finds all empty tiles in a specific radius around the player's position.
     */
    private fun findEmptyTilesInRadius(
        layout: GridLayout,
        playerPosition: Pair<Int, Int>,
        radius: Int
    ): List<View> {
        val emptyTiles = mutableListOf<View>()
        val rows = layout.rowCount
        val cols = layout.columnCount

        for (dx in -radius..radius) {
            for (dy in -radius..radius) {
                if (dx.absoluteValue + dy.absoluteValue == radius) { // Points exactly on the radius
                    val newRow = playerPosition.first + dx
                    val newCol = playerPosition.second + dy

                    if (newRow in 0 until rows && newCol in 0 until cols) {
                        val tile = layout.getChildAt(newRow * cols + newCol)
                        if (tile.tag == " ") { // Check if the tile is empty
                            emptyTiles.add(tile)
                        }
                    }
                }
            }
        }

        return emptyTiles
    }

