package com.example.sokoban.gameItems

class InventoryService {
    private val inventory: MutableList<Item> = mutableListOf() // List to store items in inventory

    // Add an item to the inventory
    fun addItem(item: Item) {
        inventory.add(item)
    }

    // Use item from inventory
    fun useItem(item: Item) = when (item.type) {
        ItemType.SPEED_BOOTS -> {
            // Example: speed boost logic
        }
        ItemType.MAGIC_CLUB -> {
            // Example: magic club logic
        }
        ItemType.MAGIC_WAND -> {
            // Example: magic wand logic
        }
        ItemType.RAY_GUN -> {
            // Example: ray gun logic
        }
    }

    // Get all items in inventory
    fun getInventory(): List<Item> {
        return inventory
    }

    //Check if item exists in inventory
    fun hasItem(itemType: ItemType): Boolean {
        return inventory.any { it.type == itemType }
    }
}