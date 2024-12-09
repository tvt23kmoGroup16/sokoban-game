package com.example.sokoban.players

import android.content.Context
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Toast
import com.example.sokoban.gameItems.ItemType
import com.example.sokoban.utils.TileUtils
import kotlin.math.absoluteValue

class Gnome(context: Context, layout: GridLayout, level: Int) : Player(context, layout, level) {

    override fun useItem(item: ItemType) {
        when (item) {
            ItemType.MAGIC_CLUB -> destroyNearestBox()
            ItemType.SPEED_BOOTS -> activateSpeedBoots()
            ItemType.MAGIC_WAND -> useMagicWand()
            else -> Toast.makeText(context, "You don't know how to use this!", Toast.LENGTH_SHORT).show()
        }
    }

    fun destroyNearestBox() {
        val playerPosition = getPlayerPosition()
        val nearestBoxTile = TileUtils.findNearestTileWithTag(layout, playerPosition, "box")

        if (nearestBoxTile != null) {
            TileUtils.clearTile(nearestBoxTile)
            Toast.makeText(context, "Smashed! You swing your club and destroy the boulder!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "No box found nearby!", Toast.LENGTH_SHORT).show()
        }
    }
}

    override fun activateSpeedBoots() {
    super.activateSpeedBoots()
    this.isSpeedBoosted = true
    this.speedBoostMovesRemaining = 3

    context?.let {
        Toast.makeText(it, "These boots wiggle on your feet! You feel fast!", Toast.LENGTH_SHORT).show()
    }
}
override fun useMagicWand() {
    super.useMagicWand()
    this.useMagicWand = true
    this.MagicWandUsesRemaining = 3

    val.emptyLocation = gameMap.findRandomEmptyTile()
    if (emptyLocation !=null) {
        gameMap.placeBox(emptyLocation)
        context?.let {
            Toast.makeText(it, "You wave the magic wand and a new boulder appears!", Toast.LENGTH_SHORT).show()
        }
    } else {
        context?.let {
            Toast.makeText(it, "No space available to create a new boulder!", Toast.LENGTH_SHORT).show()
        }
    }
}

private fun useMagicClub() {
    //TODO
    val targetBox = gameMap.findClosestBox(this.position) // Find the nearest box to the player

    if (targetBox != null) {
        gameMap.removeBox(targetBox)
        context?.let {
            Toast.makeText(it, "Crack! You smashed a boulder!", Toast.LENGTH_SHORT).show()
        }

        // Check if this was the last box
        if (gameMap.remainingBoxes == 0) {
            winGame() // Call the win method
        }
    } else {
        context?.let {
            Toast.makeText(it, "No boxes left to explode!", Toast.LENGTH_SHORT).show()
        }
    }
}
