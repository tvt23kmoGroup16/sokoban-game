package com.example.sokoban.players

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Toast
import com.example.sokoban.gameItems.Item
import com.example.sokoban.gameItems.ItemType
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

   open fun useItem(item: Item) {
        when (item.type) {
            ItemType.SPEED_BOOTS -> activateSpeedBoots(item)
            ItemType.MAGIC_WAND -> useMagicWand(item)
            ItemType.RAY_GUN -> destroyNearestBox(item, 3)
            ItemType.MAGIC_CLUB -> destroyNearestBox(item, 2)
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
                Toast.makeText(context, "Speed Boots activated! Moved 2 tiles. Uses remaining: ${item.usesLeft}.", Toast.LENGTH_SHORT).show()
            }
                else {
                Toast.makeText(context, "Cannot move with Speed Boots!", Toast.LENGTH_SHORT).show()
                }
            // After using the boots, check if there are remaining uses
                if (item.usesLeft == 0) {
                Toast.makeText(context, "Speed Boots are out of uses!", Toast.LENGTH_SHORT).show()
                }
            } else {
            Toast.makeText(context, "Speed Boots are out of uses!", Toast.LENGTH_SHORT).show()
        }
    }

    open fun useMagicWand(item: Item) {
       if (item.usesLeft > 0) {
        val targetTile = getTargetTileInDirection(direction)
        if (targetTile != null) {
            placeBox(targetTile)
            item.usesLeft = maxOf(item.usesLeft - 1, 0)
        }
            if (item.usesLeft == 0) {
                Toast.makeText(context, "Magic Wand has no more uses!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Magic Wand uses remaining: ${item.usesLeft}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "No space available to create a new boulder!", Toast.LENGTH_SHORT).show()
        }
    }

    open fun destroyNearestBox(item: Item, range: Int) {

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

        // Update appearance
        updateAppearance(newTile, isSpeedBoostActive = false)

        // Update current position
        currentRow = newRow
        currentColumn = newColumn

        moveCount ++
    }

    open fun updateAppearance(imgPlayer: ImageView, isSpeedBoostActive: Boolean) {
        try {
            val assetManager = imgPlayer.context.assets
            val imagePath = if (isSpeedBoostActive) {
                "characters/speed_boost_active.png"
            } else {
                when (playerType) {
                    PlayerType.ALIEN -> "characters/alien.png"
                    PlayerType.GNOME -> "characters/gnome.png"
                    PlayerType.HUMAN -> "characters/human.png"
                    else -> "characters/default.png"
                }
            }
            val inputStream = assetManager.open(imagePath)
            val drawable = Drawable.createFromStream(inputStream, null)
            imgPlayer.setImageDrawable(drawable)
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    fun getTargetTileInDirection(direction: Direction): View? {
        val (newRow, newCol) = when (direction) {
            Direction.UP -> currentRow - 1 to currentColumn
            Direction.DOWN -> currentRow + 1 to currentColumn
            Direction.LEFT -> currentRow to currentColumn - 1
            Direction.RIGHT -> currentRow to currentColumn + 1
        }

        return if (newRow in 0 until layout.rowCount && newCol in 0 until layout.columnCount) {
            val targetTile = layout.getChildAt(newRow * layout.columnCount + newCol)
            if (targetTile.tag == " ") targetTile else null
        } else {
            null
        }
    }

    fun placeBox(tile: View) {
        val boxBitmap = getBoxImageForLevel(level)
        if (boxBitmap != null) {
            val imageView = tile.findViewById<ImageView>(android.R.id.icon)
            imageView?.setImageBitmap(boxBitmap)
            tile.tag = "box" // Mark the tile as occupied by a box
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

    enum class PlayerType(val displayName: String) {
        ALIEN("Alien"),
        GNOME("Gnome"),
        HUMAN("Human"),
        ALL("All")
    }
}
