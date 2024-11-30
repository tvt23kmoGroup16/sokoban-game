package com.example.sokoban

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PlayerMovement(
    private val activity: AppCompatActivity,
    private val gameMap: GameMap
) {
    private var playerX = 1
    private var playerY = 1

    fun movePlayer(dx: Int, dy: Int) {
        val newX = playerX + dx
        val newY = playerY + dy
        val mapWidth = gameMap.map[0].size
        val mapHeight = gameMap.map.size

        if (newX < 0 || newX >= mapWidth || newY < 0 || newY >= mapHeight) {
            Toast.makeText(activity, "Can't move outside the map!", Toast.LENGTH_SHORT).show()
            return
        }

        if (gameMap.map[newY][newX] == '#') {
            Toast.makeText(activity, "Can't move there!", Toast.LENGTH_SHORT).show()
            return
        }

        if (gameMap.map[newY][newX] == 'B') {
            val boxNewX = newX + dx
            val boxNewY = newY + dy
            if (boxNewX in 0 until mapWidth && boxNewY in 0 until mapHeight) {
                if (gameMap.map[boxNewY][boxNewX] == ' ' || gameMap.map[boxNewY][boxNewX] == 'X') {
                    gameMap.map[boxNewY][boxNewX] = 'B'
                    gameMap.map[newY][newX] = 'P'
                    gameMap.map[playerY][playerX] = ' '
                    playerX = newX
                    playerY = newY
                } else {
                    Toast.makeText(activity, "Box can't move there!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(activity, "Box can't move out of bounds!", Toast.LENGTH_SHORT).show()
            }
        } else {
            gameMap.map[newY][newX] = 'P'
            gameMap.map[playerY][playerX] = ' '
            playerX = newX
            playerY = newY
        }

        gameMap.renderMap()

        if (checkWinCondition()) {
            Toast.makeText(activity, "You win!", Toast.LENGTH_LONG).show()
        }
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
}
