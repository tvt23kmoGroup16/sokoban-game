package com.example.sokoban

import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.sokoban.gameItems.InventoryService
import com.example.sokoban.gameItems.Item

class PlayerMovement(
    private val activity: GameActivity,
    private val gameMap: GameMap,
    private val tvGameStatus: TextView,
    val inventoryService: InventoryService
) {
    private var playerX: Int = 0  // Player's X position
    private var playerY: Int = 0  // Player's Y position
    var moveCount: Int = 0  // Variable to count moves
    private val moveHistory: MutableList<GameState> = mutableListOf()  // History of moves

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

        // item auto-pickup
        val item = "Item"
        if (gameMap.map[newY][newX] == 'I') {
            gameMap.map[newY][newX] = ' '
            inventoryService.addItem()
            Toast.makeText(activity, "You picked up an item!", Toast.LENGTH_SHORT).show()
        }

        // Check if the destination is a wall ('#')
        if (gameMap.map[newY][newX] == '#') {
            Toast.makeText(activity, "Can't move there!", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if the destination is a hole ('O')
        if (gameMap.map[newY][newX] == 'O') {
            Toast.makeText(activity, "Player fell into a hole! Game Over!", Toast.LENGTH_SHORT)
                .show()
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
                if (gameMap.map[boxNewY][boxNewX] == ' ' || gameMap.map[boxNewY][boxNewX] == 'X') {
                    when (gameMap.map[boxNewY][boxNewX]) {
                        ' ' -> {
                            // Move the box to an empty space
                            gameMap.map[boxNewY][boxNewX] = 'B'
                            gameMap.map[newY][newX] = 'P'
                            gameMap.map[playerY][playerX] = ' '

                            // Update player position
                            playerX = newX
                            playerY = newY
                        }

                        'O' -> {
                            // storing the original position of the box
                            val boxOriginalX = boxNewX
                            val boxOriginalY = boxNewY

                            // Box falls into a hole
                            gameMap.map[boxNewY][boxNewX] = ' '  // Hole becomes empty
                            gameMap.map[newY][newX] = 'P'
                            gameMap.map[playerY][playerX] = ' '

                            // Check for magic wand
                            val wand = inventoryService.getItemByName("Magic Wand")
                            if (wand != null && wand.usesLeft > 0) {
                                wand.usesLeft -= 1
                                Log.d(
                                    "PlayerMovement",
                                    "Magic Wand used. Remaining uses: ${wand.usesLeft}"
                                )
                                // Box falls into the hole, but the player used the magic wand to stop the game over
                                Toast.makeText(
                                    activity,
                                    "The box fell into the hole, but you used the Magic Wand!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                //Set the box to its original position
                                gameMap.map[boxOriginalY][boxOriginalX] = 'B'
                            } else {
                                // No magic wand or no uses left
                                if (wand == null) {
                                    Toast.makeText(
                                        activity,
                                        "No Magic Wand in inventory!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        activity,
                                        "Magic Wand has no uses left! Game Over!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                // Proceeding to Game over
                                Log.d(
                                    "PlayerMovement",
                                    "Game Over: No magic wand or no uses left after box fell into hole."
                                )
                                activity.showGameOverDialog()
                                return
                            }
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
            } else {
                // Handle normal player movement
                gameMap.map[newY][newX] = 'P'  // Move player to new position
                gameMap.map[playerY][playerX] = ' '  // Empty the old position

                // Update player position
                playerX = newX
                playerY = newY

                // Increment move count and save game state
                incrementMoveCount()
                saveState()
            }

            // Render the updated map and update game status
            gameMap.renderMap()
            updateMoveCount()

            // Check for win condition
            if (checkWinCondition()) {
                activity.showWinDialog(moveCount)
            }

            // Check for no moves left
            if (checkForNoMovesLeft()) {
                Toast.makeText(activity, "No moves left! Game over!", Toast.LENGTH_SHORT).show()
                activity.showGameOverDialog()
            }
        }
    }

    fun checkForNoMovesLeft(): Boolean {
        val directions = listOf(
            Pair(0, -1),  // Up
            Pair(0, 1),   // Down
            Pair(-1, 0),  // Left
            Pair(1, 0)    // Right
        )
        val mapWidth = gameMap.map[0].size
        val mapHeight = gameMap.map.size

        for ((dx, dy) in directions) {
            val newX = playerX + dx
            val newY = playerY + dy

            // Is the move between bounds
            if (newX in 0 until mapWidth && newY in 0 until mapHeight) {
                when (gameMap.map[newY][newX]) {
                    ' ' -> return false  // Open space to move
                    'B' -> {
                        val boxNewX = newX + dx
                        val boxNewY = newY + dy

                        // Is the box pushable
                        if (boxNewX in 0 until mapWidth && boxNewY in 0 until mapHeight &&
                            (gameMap.map[boxNewY][boxNewX] == ' ' || gameMap.map[boxNewY][boxNewX] == 'X')
                        ) {
                            return false  // Can be pushed
                        }
                    }
                }
            }
        }
        // No valid moves left
        return true
    }

    fun incrementMoveCount() {
        moveCount++
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

    // Data class to hold game state (player position and map state)
    data class GameState(
        val mapState: Array<CharArray>,  // Game map state
        val playerPosition: Pair<Int, Int>,  // (playerX, playerY)
        val moveCount: Int
    )
}


