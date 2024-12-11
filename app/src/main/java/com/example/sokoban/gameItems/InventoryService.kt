package com.example.sokoban.gameItems

class InventoryService {
    private val items: MutableList<Item> = mutableListOf() // List to store items in inventory

    // Add an item to the inventory
    fun addItem(item: Item) {
        items.add(item)
    }

    // Get an item by name (returns null if not found)
    fun getItemByName(name: String): Item? {
        return items.find { it.name == name }
    }

    // Check if an item exists in the inventory by name
    fun hasItem(name: String): Boolean {
        return items.any { it.name == name }
    }

    // Remove an item from the inventory by name
    fun removeItem(name: String) {
        items.removeAll { it.name == name }
    }

    // Get the entire inventory
    fun getInventory(): List<Item> {
        return items
    }
}
