package com.example.sokoban.utils

import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import com.example.sokoban.players.Player
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
                    if (tile.tag == "B") {
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

/**
 * Finds the nearest tile with a specific tag.
 */
    fun findNearestTileWithTag(
        layout: GridLayout,
        playerPosition: Pair<Int, Int>,
        tag: String,
        range: Int
    ): View? {
        // Assuming that the range is the maximum distance you're willing to search
        val (playerRow, playerColumn) = playerPosition
        for (r in (playerRow - range)..(playerRow + range)) {
            for (c in (playerColumn - range)..(playerColumn + range)) {
                // Check if the tile is within bounds
                if (r in 0 until layout.rowCount && c in 0 until layout.columnCount) {
                    val tile = layout.getChildAt(r * layout.columnCount + c)
                    if (tile.tag == tag) {
                        return tile
                    }
                }
            }
        }
        return null // Return null if no box is found within the range
    }


/**
 * Finds all tiles within a specific range and direction, considering obstacles.
 */
fun findTileInRange(
    layout: GridLayout,
    startRow: Int,
    startColumn: Int,
    range: Int,
    direction: Player.Direction,
    obstacleTag: String? = null
): View? {
    val rowCount = layout.rowCount
    val columnCount = layout.columnCount

    for (distance in 1..range) {
        val targetRow: Int = when (direction) {
            Player.Direction.UP -> startRow - distance
            Player.Direction.DOWN -> startRow + distance
            Player.Direction.LEFT -> startRow
            Player.Direction.RIGHT -> startRow
        }

        val targetColumn: Int = when (direction) {
            Player.Direction.UP, Player.Direction.DOWN -> startColumn
            Player.Direction.LEFT -> startColumn - distance
            Player.Direction.RIGHT -> startColumn + distance
        }

        // Check if the target is within bounds
        if (targetRow in 0 until rowCount && targetColumn in 0 until columnCount) {
            val index = targetRow * columnCount + targetColumn
            val targetTile = layout.getChildAt(index)

            // Check for null safety
            if (targetTile != null) {
                // If an obstacle is encountered, stop searching further
                if (obstacleTag != null && targetTile.tag == obstacleTag) {
                    break
                }

                // Return the first tile with the specified tag
                if (targetTile.tag == "B") {
                    return targetTile
                }
            }
        }
    }
    return null // No valid tile found
}

/**
 * Counts remaining boxes on the Grid layout
 */
fun countRemainingBoxes(layout: GridLayout): Int {
    var count = 0

    for (row in 0 until layout.rowCount) {
        for (col in 0 until layout.columnCount) {
            val tile = layout.getChildAt(row * layout.columnCount + col)
            if (tile?.tag == 'B') {
                count++
            }
        }
    }
    return count
}
