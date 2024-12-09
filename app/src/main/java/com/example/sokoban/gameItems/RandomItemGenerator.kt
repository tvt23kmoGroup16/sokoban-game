package com.example.sokoban.gameItems

import com.example.sokoban.players.Player
import kotlin.random.Random

class RandomItemGenerator {

    // Item probabilities
    private val rayGunProbability = 25 // 30% chance for Ray Gun (only if Alien is in play)
    private val magicClubProbability = 25 // 30% chance for Magic Club (only if Gnome is in play)
    private val speedBootsProbability = 10 // 10% chance for Speed Boots (for both Alien and Gnome)
    private val magicWandProbability = 20 // 20% chance for Magic Wand (for both Alien and Gnome)

    fun getRandomItem(player: Player): ItemType? {
        val rand = Random.nextInt(100) // Random number between 0 and 100

        return when {
            player is com.example.sokoban.players.Alien && rand < rayGunProbability -> ItemType.RAY_GUN
            player is com.example.sokoban.players.Gnome && rand < magicClubProbability -> ItemType.MAGIC_CLUB
            rand < rayGunProbability + magicClubProbability + speedBootsProbability -> ItemType.SPEED_BOOTS
            rand < rayGunProbability + magicClubProbability + speedBootsProbability + magicWandProbability -> ItemType.MAGIC_WAND
            else -> null // No item
        }
    }
}