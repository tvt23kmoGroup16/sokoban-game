package com.example.sokoban.gameItems

import android.widget.Toast
import com.example.sokoban.GameActivity
import com.example.sokoban.players.Player


class InventoryService(private val gameActivity: GameActivity) {

    private val inventory = mutableListOf<Item>()

    // Add item to inventory
    fun addItem(item: Item) {
        inventory.add(item)
        Toast.makeText(gameActivity, "You picked up a ${item.name}.", Toast.LENGTH_SHORT).show()
    }

    // Remove item from inventory
    fun removeItem(item: Item) {
        inventory.remove(item)
        Toast.makeText(gameActivity, "You used the ${item.name}.", Toast.LENGTH_SHORT).show()
    }

    // Check if an item exists in inventory
    fun hasItem(item: Item): Boolean {
        return item in inventory
    }

    fun useItem(player: Player, item: Item) {
        if (hasItem(item)) {
            item.type.action(player, gameActivity, item)
            if (item.usesLeft <= 0) {
                item.usesLeft = 0
                removeItem(item) // Remove the item if it's used up
            }
        } else {
            Toast.makeText(gameActivity, "You don't have any ${item.type.displayName} to use!", Toast.LENGTH_SHORT).show()
        }
    }

    // List the current items
    fun listItems(): List<Item> {
        return inventory
    }
}