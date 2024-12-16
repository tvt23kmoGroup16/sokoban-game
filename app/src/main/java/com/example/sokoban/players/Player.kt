package com.example.sokoban.players

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Toast
import com.example.sokoban.GameActivity
import com.example.sokoban.gameItems.Item
import com.example.sokoban.gameItems.ItemType
import com.example.sokoban.utils.TileUtils
import com.example.sokoban.utils.countRemainingBoxes
import com.example.sokoban.utils.findNearestTileWithTag
import java.io.IOException
import java.io.InputStream


open class Player(
    val context: Context,
    val layout: GridLayout,
    val level: Int,
    val playerType: PlayerType
) {
    var currentRow: Int = 0
    var currentColumn: Int = 0
    val direction: Direction = Direction.RIGHT // Default
    var moveCount: Int = 0


    enum class Direction {
        UP, DOWN, LEFT, RIGHT
    }

   open fun useItem(item: Item, range: Int = 0) {
        when (item.type) {
            ItemType.SPEED_BOOTS -> activateSpeedBoots(item)
            ItemType.MAGIC_WAND -> useMagicWand(item)
            ItemType.RAY_GUN -> useRayGun(item, range)
            ItemType.MAGIC_CLUB -> useMagicClub(item, range)
            else -> {
                Toast.makeText(context, "You cannot use this item!", Toast.LENGTH_SHORT).show()
            }
        }
   }
    open fun activateSpeedBoots(item: Item) {
        if (item.usesLeft > 0) {
            val newPosition = getNewPositionForSpeedBoost()
            if (newPosition != null) {
                movePlayerToNewPosition(newPosition)
                item.usesLeft = maxOf(item.usesLeft - 1, 0)
                Toast.makeText(context, "Speed Boots activated! Moved 2 tiles.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(context, "Cannot move with Speed Boots!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    open fun useMagicWand(item: Item) {
       if (item.usesLeft > 0) {
        val targetTile = getTargetTileInDirection(direction)
        if (targetTile != null) {
            placeBox(targetTile)
            item.usesLeft = maxOf(item.usesLeft - 1, 0)
        }
        } else {
            Toast.makeText(context, "No space available to create a new boulder!", Toast.LENGTH_SHORT).show()
        }
    }

    fun useItemWithFeedback(item: Item, itemName: String, action: () -> Unit) {
        if (item.usesLeft > 0) {
            action()
            Toast.makeText(context, "$itemName uses remaining: ${item.usesLeft}", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "$itemName is out of uses!", Toast.LENGTH_SHORT).show()
        }
    }
    // Base Ray Gun usage, but should be overridden by subclasses
    open fun useRayGun(item: Item, range: Int) {
        // Default behavior (might be overridden in Alien class)
        Toast.makeText(context, "Ray Gun shot!", Toast.LENGTH_SHORT).show()
        item.usesLeft = maxOf(item.usesLeft - 1, 0)
    }

    // Base Magic Club usage, but should be overridden by subclasses
    open fun useMagicClub(item: Item, range: Int) {
        // Default behavior (might be overridden in Gnome class)
        Toast.makeText(context, "Magic Club activated!", Toast.LENGTH_SHORT).show()
        item.usesLeft = maxOf(item.usesLeft - 1, 0)
    }

    fun getPlayerPosition(): Pair<Int, Int> {
        return Pair(currentRow, currentColumn)
    }

    open fun getNewPositionForSpeedBoost(): Pair<Int, Int>? {
        val rowCount = layout.rowCount
        val columnCount = layout.columnCount

        val newRow = currentRow // Logic to calculate new row
        val newColumn = currentColumn // Logic to calculate new column

        return if (newRow in 0 until rowCount && newColumn in 0 until columnCount) {
            Pair(newRow, newColumn)
        } else {
            null
        }
    }

    open fun movePlayerToNewPosition(newPosition: Pair<Int, Int>) {
        val newRow = newPosition.first
        val newColumn = newPosition.second

        val newTile = layout.getChildAt(newRow * layout.columnCount + newColumn) as ImageView

        // Removing player from the old position
        val oldTile = layout.getChildAt(currentRow * layout.columnCount + currentColumn) as ImageView
        oldTile.setImageBitmap(null)

        // Update current position
        currentRow = newRow
        currentColumn = newColumn

        moveCount ++
    }


    open fun getTargetTileInDirection(direction: Direction): View? {
        val (newRow, newCol) = when (direction) {
            Direction.UP -> currentRow - 1 to currentColumn
            Direction.DOWN -> currentRow + 1 to currentColumn
            Direction.LEFT -> currentRow to currentColumn - 1
            Direction.RIGHT -> currentRow to currentColumn + 1
        }

        return if (newRow in 0 until layout.rowCount && newCol in 0 until layout.columnCount) {
            val targetTile = layout.getChildAt(newRow * layout.columnCount + newCol)
            if (targetTile.tag == " ") targetTile else null  // Check if tile is empty (tag == " ")
        } else {
            null
        }
    }

    fun placeBox(tile: View) {
        val boxBitmap = getBoxImageForLevel(level)
        if (boxBitmap != null) {
            val imageView = tile.findViewById<ImageView>(android.R.id.icon)
            imageView?.setImageBitmap(boxBitmap)
            tile.tag = "B" // Mark the tile as occupied by a box
            Toast.makeText(context, "You wave the wand and create a boulder!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Error loading box image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getBoxImageForLevel(level: Int): Bitmap? {
        return try {
            val assetManager = context.assets
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
            null
        }
    }

    fun checkWinCondition() {
        val remainingBoxes = countRemainingBoxes(layout)
        if (remainingBoxes == 0) {
            (context as? GameActivity)?.showWinDialog(moveCount)
        } else {
            Toast.makeText(context, "$remainingBoxes boulders remaining.", Toast.LENGTH_SHORT)
                .show()
        }
    }
    enum class PlayerType(val displayName: String) {
        ALIEN("Alien"),
        GNOME("Gnome"),
        ALL("All")
    }
}



