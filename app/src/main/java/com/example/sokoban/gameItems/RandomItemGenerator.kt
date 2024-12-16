package com.example.sokoban.gameItems

import com.example.sokoban.PlayerMovement
import com.example.sokoban.players.Alien
import com.example.sokoban.players.Gnome
import com.example.sokoban.players.Player
import kotlin.random.Random

open class RandomItemGenerator(private var level: Int, private var stepsTaken: Int) {
    var itemsPlaced = false

    private val baseRayGunProbability = 25
    private val baseMagicClubProbability = 25
    private val baseSpeedBootsProbability = 10
    private val baseMagicWandProbability = 20

    private val levelIncreaseFactor = 5
    private val stepsIncreaseFactor = 5


    private val itemProbabilities = mapOf(
        ItemType.RAY_GUN to baseRayGunProbability,
        ItemType.MAGIC_CLUB to baseMagicClubProbability,
        ItemType.SPEED_BOOTS to baseSpeedBootsProbability,
        ItemType.MAGIC_WAND to baseMagicWandProbability
    )


    fun updateLevelAndSteps(newLevel: Int, newStepsTaken: Int) {
        this.level = newLevel
        this.stepsTaken = newStepsTaken
    }

    open fun placeRandomItems(
        map: Array<CharArray>,
        level: Int,
        stepsTaken: Int
    ) {
        if (itemsPlaced) return
        val itemCount = calculateItemCount(level, stepsTaken)
        val availableCells = mutableListOf<Pair<Int, Int>>()

        //collecting all the available cells
        map.forEachIndexed { row, cols ->
            cols.forEachIndexed { col, cell ->
                if (cell == ' ') {  // Only empty spaces or floor tiles
                    availableCells.add(row to col)
                }
            }
        }

        repeat(itemCount) {
            if (availableCells.isEmpty()) return  // Stop if no available cells remain

            // Select a random cell from availableCells
            val randomIndex = (availableCells.indices).random()
            val (row, col) = availableCells.removeAt(randomIndex)

            // Generate a random item
            val itemType = generateRandomItem(level, stepsTaken)

            // Place the item and log placement if successful
            itemType?.let {
                map[row][col] = getItemKey(it) // Convert the item to a map character
                logItemPlacement(row, col, it) // Debug
            }
        }
        itemsPlaced = true
    }


    private fun logItemPlacement(row: Int, col: Int, item: ItemType) {
        println("Placed ${item.name} at ($row, $col)")
    }

    open fun calculateItemCount(level: Int, stepsTaken: Int): Int {
        // Starting with 1 item and increasing the count based on level and steps
        val baseItemCount = 1
        val levelFactor = level / 2  // more items per level
        val stepsFactor = stepsTaken / 100  // more items based on steps taken

        // Ensure at least one item is placed
        return maxOf(baseItemCount + levelFactor + stepsFactor, 1)
    }

    open fun generateRandomItem(level: Int, stepsTaken: Int): ItemType? {
        val adjustedProbabilities = itemProbabilities.mapValues {
            calculateAdjustedProbability(it.value, level, stepsTaken)
        }

        // Create cumulative probability distribution
        val cumulativeProbabilities = adjustedProbabilities.entries.fold(mutableListOf<Pair<Int, ItemType>>()) { acc, entry ->
            val cumulative = (acc.lastOrNull()?.first ?: 0) + entry.value
            acc.apply { add(cumulative to entry.key) }
        }

        //Picking an item randomly
        val rand = Random.nextInt(cumulativeProbabilities.last().first)
        return cumulativeProbabilities.firstOrNull { rand < it.first }?.second
    }

    //Adjusting probability based on level and taken steps
    private fun calculateAdjustedProbability(baseProbability: Int, level: Int, stepsTaken: Int): Int {
        return baseProbability + (level * levelIncreaseFactor) + (stepsTaken / 50 * stepsIncreaseFactor)
    }


    //determining the item based on player type and adjusted probability
    fun getRandomItem(player: Player, level: Int, stepsTaken: Int): ItemType? {
        val adjustedProbabilities = itemProbabilities.mapValues {
            calculateAdjustedProbability(it.value, level, stepsTaken)
        }


        val rand = Random.nextInt(100)

        //safely access the adjusted probabilities
        val rayGunProbability = adjustedProbabilities[ItemType.RAY_GUN] ?: 0
        val magicClubProbability = adjustedProbabilities[ItemType.MAGIC_CLUB] ?: 0

        return when {
            player is Alien && rand < rayGunProbability -> ItemType.RAY_GUN
            player is Gnome && rand < magicClubProbability -> ItemType.MAGIC_CLUB
            rand < adjustedProbabilities.values.sum() -> generateRandomItem(level, stepsTaken)
            else -> null //nothing generated
        }
    }
}
//mapping item types to item keys
private fun getItemKey(item: ItemType): Char {
            return when (item) {
                ItemType.RAY_GUN -> 'R'
                ItemType.MAGIC_CLUB -> 'M'
                ItemType.SPEED_BOOTS -> 'S'
                ItemType.MAGIC_WAND -> 'W'
            }
        }


