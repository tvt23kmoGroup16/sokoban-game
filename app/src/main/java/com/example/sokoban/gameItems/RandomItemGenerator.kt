package com.example.sokoban.gameItems

import com.example.sokoban.players.Player
import com.example.sokoban.players.Alien
import com.example.sokoban.players.Gnome
import kotlin.random.Random

open class RandomItemGenerator {

        private val baseRayGunProbability = 25
        private val baseMagicClubProbability = 25
        private val baseSpeedBootsProbability = 10
        private val baseMagicWandProbability = 20

        private val levelIncreaseFactor = 5
        private val stepsIncreaseFactor = 5


       open fun placeRandomItems(
            map: Array<CharArray>,
            itemCount: Int,
            level: Int,
            stepsTaken : Int
        ) {
           val itemCount = calculateItemCount(level, stepsTaken)

            val availableCells = map.indices.flatMap { row ->  // collecting all available positions on the map for the item to be placed
                map[row].indices.mapNotNull { col -> if (map[row][col] == ' ') row to col else null }  //flatMap goes through each row and column
            }.toMutableList()

            repeat(itemCount) {         //Looping times itemCount
                if (availableCells.isNotEmpty()) {  // placing items in random positions
                    val randomIndex = Random.nextInt(availableCells.size)   // finding a new random position for every loop iteration
                    val (row, col) = availableCells.removeAt(randomIndex)      // removing available cells from the list so the item does not get placed to same cell twice

                    val itemType = generateRandomItem(level, stepsTaken)  //generating random item based on calculations
                    itemType?.let { map[row][col] = getItemKey(it) }  // converting the item, ex. 'R' for Ray Gun
                }
            }
        }
        open fun calculateItemCount(level: Int, stepsTaken: Int): Int {
        // Starting with 1 item and increasing the count based on level and steps
        val baseItemCount = 1
        val levelFactor = level / 2  // more items per level
        val stepsFactor = stepsTaken / 100  // more items based on steps taken

        // Ensure at least one item is placed
        return maxOf(baseItemCount + levelFactor + stepsFactor, 1)
    }
        fun getRandomItem(player: Player, level: Int, stepsTaken: Int): ItemType? {
        val rand = Random.nextInt(100) // Random number between 0 and 100
        val adjustedRayGunProbability = baseRayGunProbability + (level * levelIncreaseFactor) + (stepsTaken / 50 * stepsIncreaseFactor)
        val adjustedMagicClubProbability = baseMagicClubProbability + (level * levelIncreaseFactor) + (stepsTaken / 50 * stepsIncreaseFactor)

        return when {
            player is Alien && rand < adjustedRayGunProbability -> ItemType.RAY_GUN
            player is Gnome && rand < adjustedMagicClubProbability -> ItemType.MAGIC_CLUB
            rand < adjustedRayGunProbability + adjustedMagicClubProbability + baseSpeedBootsProbability -> ItemType.SPEED_BOOTS
            rand < adjustedRayGunProbability + adjustedMagicClubProbability + baseSpeedBootsProbability + baseMagicWandProbability -> ItemType.MAGIC_WAND
            else -> null
        }
    }

        private fun generateRandomItem(level: Int, stepsTaken: Int): ItemType? {
            val rand = Random.nextInt(100)

            val rayGunProb = calculateAdjustedProbability(baseRayGunProbability, level, stepsTaken)
            val magicClubProb = rayGunProb + calculateAdjustedProbability(baseMagicClubProbability, level, stepsTaken)
            val speedBootsProb = magicClubProb + calculateAdjustedProbability(baseSpeedBootsProbability, level, stepsTaken)
            val magicWandProb = speedBootsProb + calculateAdjustedProbability(baseMagicWandProbability, level, stepsTaken)

            return when {
                rand < rayGunProb -> ItemType.RAY_GUN
                rand < magicClubProb -> ItemType.MAGIC_CLUB
                rand < speedBootsProb -> ItemType.SPEED_BOOTS
                rand < magicWandProb -> ItemType.MAGIC_WAND
                else -> null
            }
        }

        private fun calculateAdjustedProbability(baseProbability: Int, level: Int, stepsTaken: Int): Int {
            return baseProbability + (level * levelIncreaseFactor) + (stepsTaken / 50 * stepsIncreaseFactor)
        }



        private fun getItemKey(item: ItemType): Char {
            return when (item) {
                ItemType.RAY_GUN -> 'R'
                ItemType.MAGIC_CLUB -> 'M'
                ItemType.SPEED_BOOTS -> 'S'
                ItemType.MAGIC_WAND -> 'W'
            }
        }
    }


