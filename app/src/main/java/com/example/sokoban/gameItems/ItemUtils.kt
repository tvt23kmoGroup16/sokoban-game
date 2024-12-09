package com.example.sokoban.gameitems

import com.example.sokoban.gameItems.Item
import com.example.sokoban.gameItems.ItemType
import com.example.sokoban.players.Alien
import com.example.sokoban.players.Gnome
import com.example.sokoban.players.Player
import kotlin.random.Random

fun createRayGun(): Item {
    return Item(ItemType.RAY_GUN, "Ray Gun") { player ->
        if (player is Alien) {
            player.explodeBox()  // Call Alien Monster's method to explode a box
        } else {
            Toast.makeText(player.context, "Only Aliens can use the Ray Gun!", Toast.LENGTH_SHORT).show()
        }
    }
}

fun createMagicClub(): Item {
    return Item(ItemType.MAGIC_CLUB, "Magic Club") { player ->
        if (player is Gnome) {
            player.smashBox()  // Call Gnome's method to smash a box
        } else {
            Toast.makeText(player.context, "Only Gnomes can use the Magic Club!", Toast.LENGTH_SHORT).show()
        }
    }
}

fun createSpeedBoots(): Item {
    return Item(ItemType.SPEED_BOOTS, "Speed Boots") { player ->
        player.activateSpeedBoots()  // Activate speed boots for the player
    }
}

fun createMagicWand(): Item {
    return Item(ItemType.MAGIC_WAND, "Magic Wand") { player ->
        player.createBox()  // Create a new box at a valid location
    }
}

fun getRandomItem(player: Player): Item? {
    val rand = Random.nextInt(100) // Get a random number between 0 and 100

    // Define probability for each item
    val rayGunProbability = 30 // 30% chance for Ray Gun (only if Alien is in play)
    val magicClubProbability = 30 // 30% chance for Magic Club (only if Gnome is in play)
    val speedBootsProbability = 20 // 20% chance for Speed Boots (for both Alien and Gnome)
    val magicWandProbability = 20 // 20% chance for Magic Wand (for both Alien and Gnome)

    return when {
        player is Alien && rand < rayGunProbability -> createRayGun()
        player is Gnome && rand < magicClubProbability -> createMagicClub()
        rand < speedBootsProbability -> createSpeedBoots()
        rand < magicWandProbability -> createMagicWand()
        else -> null // No item
    }
}
