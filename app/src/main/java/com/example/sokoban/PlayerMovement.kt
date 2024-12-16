package com.example.sokoban

import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.sokoban.gameItems.RandomItemGenerator


class PlayerMovement (
    private val activity: GameActivity,
    private val gameMap: GameMap,
    private val tvGameStatus: TextView
) {
    private var playerX: Int = 0  // Player's X position
    private var playerY: Int = 0  // Player's Y position
    var moveCount: Int = 0  // Variable to count moves
    private val moveHistory: MutableList<GameState> = mutableListOf()  // History of moves

    private lateinit var randomItemGenerator: RandomItemGenerator

    init {
        findInitialPlayerPosition()
        saveState()
    }

    private fun findInitialPlayerPosition() {
        // Scan the map for the 'P' character and save its position
        outerLoop@ for (y in gameMap.map.indices) {
            for (x in gameMap.map[y].indices) {
                if (gameMap.map[y][x] == 'P') {
                    playerX = x  // Set player X position
                    playerY = y  // Set player Y position
                    break@outerLoop  // Exit both loops when player is found
                }
            }
        }
    }

    // Function to store the current game state
    fun saveState() {
        val mapStateCopy =
            Array(gameMap.map.size) { gameMap.map[it].clone() } // Create copy of game map
        val playerPosition = Pair(playerX, playerY)  // Store player position
        val currentMoveCount = moveCount  // Store the move count

        val gameState = GameState(mapStateCopy, playerPosition, currentMoveCount)
        moveHistory.add(gameState)
    }

    fun undoMove() {
        if (moveHistory.size > 1) { // Ensure at least two states exist (current + one to undo to)
            moveHistory.removeAt(moveHistory.size - 1) // Remove the current state
            val lastMove = moveHistory.last() // Access the last saved state

            // Restore player position and map state
            val (savedX, savedY) = lastMove.playerPosition
            playerX = savedX
            playerY = savedY
            moveCount = lastMove.moveCount
            gameMap.map = lastMove.mapState

            // Re-render the map and update the game status
            gameMap.renderMap()
            updateMoveCount()
        } else {
            Toast.makeText(activity, "No moves to undo!", Toast.LENGTH_SHORT).show()
        }
    }

    fun movePlayer(dx: Int, dy: Int) {

        val newX = playerX + dx
        val newY = playerY + dy
        val mapWidth = gameMap.map[0].size
        val mapHeight = gameMap.map.size

        // Check if the move is within the map bounds
        if (newX < 0 || newX >= mapWidth || newY < 0 || newY >= mapHeight) {
            Toast.makeText(activity, "Can't move outside the map!", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if the destination is a wall ('#')
        if (gameMap.map[newY][newX] == '#') {
            Toast.makeText(activity, "Can't move there!", Toast.LENGTH_SHORT).show()
            return
        }
        if (gameMap.map[newY][newX] == 'O') {
            Toast.makeText(activity, "Player fell into a hole! Game Over!", Toast.LENGTH_SHORT).show()
            Log.d("PlayerMovement", "Player fell into a hole at ($newX, $newY).")
            activity.showGameOverDialog()
            return
        }

        Log.d("PlayerMovement", "Player moved to: ($playerX, $playerY)")

        // Handle box pushing
        if (gameMap.map[newY][newX] == 'B') {
            val boxNewX = newX + dx
            val boxNewY = newY + dy

            // Check if the new box position is within bounds
            if (boxNewX in 0 until mapWidth && boxNewY in 0 until mapHeight) {
                // Check if the new position for the box is a hole ('O')
                if (gameMap.map[boxNewY][boxNewX] == 'O') {
                    Toast.makeText(activity, "Box fell into a hole! Game Over!", Toast.LENGTH_SHORT)
                        .show()
                    Log.d("PlayerMovement", "Box fell into a hole at ($boxNewX, $boxNewY).")
                    activity.showGameOverDialog()
                    return
                }

                if (gameMap.map[boxNewY][boxNewX] == ' ' || gameMap.map[boxNewY][boxNewX] == 'X') {

                    when (gameMap.map[boxNewY][boxNewX]) {
                        ' ' -> {
                            // Move the box to an empty space
                            gameMap.map[boxNewY][boxNewX] = 'B'
                            gameMap.map[newY][newX] = 'P'
                            gameMap.map[playerY][playerX] = ' '

                        }

                        else -> {
                            Toast.makeText(activity, "Box can't move there!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                } else {
                    Toast.makeText(activity, "Box can't move out of bounds!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        } else {
        // Handle normal player movement (if the player is not interacting with a box)
        gameMap.map[newY][newX] = 'P'
            }
        // Check if the destination has an item
            if (gameMap.map[newY][newX] in listOf('S', 'M', 'R', 'W')) {
                pickupItem(newX, newY)
            }
            // Handle normal player movement
            gameMap.map[newY][newX] = 'P'  // Move player to new position

            // Restore the previous position
            if (gameMap.goalPositions.any { it.first == playerX && it.second == playerY }) {
                // If the previous position was a goal, restore it
                gameMap.map[playerY][playerX] = 'X'
            } else {
                // Otherwise, make it an empty tile
                gameMap.map[playerY][playerX] = ' '
            }

            // Update player position
            playerX = newX
            playerY = newY

            // Increment move count and save game state
            incrementMoveCount()
            saveState()

        // Render the updated map and update game status
        gameMap.renderMap()
        updateMoveCount()

        randomItemGenerator = RandomItemGenerator(1, moveCount)
        // Place random items after moving
        if (moveCount % 200 == 0 && moveCount != 0) {
            Log.d("ItemPlacement", "Placing random items after $moveCount moves")
            randomItemGenerator.placeRandomItems(gameMap.map, 1, moveCount)
        }
        randomItemGenerator.placeRandomItems(gameMap.map, 1, 50,)

        // Check for win condition
        if (checkWinCondition()) {
            activity.showWinDialog(moveCount)
        }
    }

    fun incrementMoveCount() {
        moveCount++
        Log.d("MoveCount", "Move count: $moveCount")
    }

    fun updateMoveCount() {
        tvGameStatus.text = "Moves: $moveCount"
    }

    private fun checkWinCondition(): Boolean {
        for (goal in gameMap.goalPositions) {
            val (goalX, goalY) = goal
            if (gameMap.map[goalY][goalX] != 'B') {
                return false
            }
        }
        return true
    }


    // Handle item pickup
    fun pickupItem(x: Int, y: Int) {
        // Retrieve the item type from the map
        val itemType = gameMap.map[y][x]
        // Perform actions based on the item type
        when (itemType) {
            'S' -> {
                // Perform action for Speed Boots
                Toast.makeText(activity, "You picked up Speed Boots!", Toast.LENGTH_SHORT).show()

            }
            'M' -> {
                // Perform action for Magic Club
                Toast.makeText(activity, "You picked up a Magic Club!", Toast.LENGTH_SHORT).show()

            }
            'W' -> {
                // Perform action for Magic Wand
                Toast.makeText(activity, "You picked up a Magic Wand!", Toast.LENGTH_SHORT).show()
            }
            'R' -> {
                // Perform action for Ray Gun
                Toast.makeText(activity, "You picked up a Ray Gun!", Toast.LENGTH_SHORT).show()

            }
            else -> {
                Toast.makeText(activity, "Unknown item type!", Toast.LENGTH_SHORT).show()
            }
        }

        // After pickup, replace the item cell with a floor tile
        gameMap.map[y][x] = ' '  // Replace item with floor space
    }


    // Data class to hold game state (player position and map state)
    data class GameState(
        val mapState: Array<CharArray>,  // Game map state
        val playerPosition: Pair<Int, Int>,  // (playerX, playerY)
        val moveCount: Int
    )
}


