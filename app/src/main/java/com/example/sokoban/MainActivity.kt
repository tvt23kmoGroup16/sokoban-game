package com.example.sokoban

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sokoban.ui.theme.SokobanTheme
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private var playerX = 1
    private var playerY = 1

    // Map: P = player, B = box, X = goal, # = wall
    private var map = arrayOf(
        charArrayOf('#', '#', '#', '#', '#'),
        charArrayOf('#', 'P', ' ', ' ', '#'),
        charArrayOf('#', ' ', 'B', ' ', '#'),
        charArrayOf('#', ' ', ' ', ' ', '#'),
        charArrayOf('#', ' ', ' ', ' ', '#'),
        charArrayOf('#', ' ', ' ', 'X', '#'),
        charArrayOf('#', '#', '#', '#', '#')
    )

    // List to track the positions of goals
    private val goalPositions = mutableListOf<Pair<Int, Int>>()

    private val mapWidth = map[0].size
    private val mapHeight = map.size

    private lateinit var tvGameMap: TextView
    private lateinit var tvGameStatus: TextView
    private lateinit var btnUp: Button
    private lateinit var btnDown: Button
    private lateinit var btnLeft: Button
    private lateinit var btnRight: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Views by ID (res/layout/activity_main.xml)
        tvGameMap = findViewById(R.id.tv_game_map)
        tvGameStatus = findViewById(R.id.tv_game_status)
        btnUp = findViewById(R.id.btn_up)
        btnDown = findViewById(R.id.btn_down)
        btnLeft = findViewById(R.id.btn_left)
        btnRight = findViewById(R.id.btn_right)

        // Save goal positions
        findGoalPositions()

        // Load game map
        showMap()

        btnUp.setOnClickListener { movePlayer(0, -1) }
        btnDown.setOnClickListener { movePlayer(0, 1) }
        btnLeft.setOnClickListener { movePlayer(-1, 0) }
        btnRight.setOnClickListener { movePlayer(1, 0) }
    }

    // Find all goal positions in the map
    private fun findGoalPositions() {
        goalPositions.clear()
        for (y in map.indices) {
            for (x in map[y].indices) {
                if (map[y][x] == 'X') {
                    goalPositions.add(Pair(x, y)) // Store coordinates
                }
            }
        }
    }

    private fun showMap() {
        tvGameMap.text = renderMap()
    }

    private fun movePlayer(dx: Int, dy: Int) {
        val newX = playerX + dx
        val newY = playerY + dy

        // Prevent index out of bounds
        if (newX < 0 || newX >= mapWidth || newY < 0 || newY >= mapHeight) {
            Toast.makeText(this, "Can't move outside the map!", Toast.LENGTH_SHORT).show()
            return
        }

        // Wall
        if (map[newY][newX] == '#') {
            Toast.makeText(this, "Can't move there!", Toast.LENGTH_SHORT).show()
            return
        }

        if (map[newY][newX] == 'B') {
            val boxNewX = newX + dx
            val boxNewY = newY + dy
            // Can box move?
            if (boxNewX in 0 until mapWidth && boxNewY in 0 until mapHeight) {
                if (map[boxNewY][boxNewX] == ' ' || map[boxNewY][boxNewX] == 'X') {
                    map[boxNewY][boxNewX] = 'B' // Place the box in the new spot
                    map[newY][newX] = 'P' // Move player to box's spot
                    map[playerY][playerX] = ' ' // Empty the previous player position
                    playerX = newX
                    playerY = newY
                } else {
                    Toast.makeText(this, "Box can't move there!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Box can't move out of bounds!", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Just move the player
            map[newY][newX] = 'P'
            map[playerY][playerX] = ' '
            playerX = newX
            playerY = newY
        }

        // Update view
        showMap()

        // Check win condition
        if (checkWinCondition()) {
            tvGameStatus.text = "You Win!"
            Toast.makeText(this, "You win!", Toast.LENGTH_LONG).show()
        }
    }

    // Render the map as string
    private fun renderMap(): String {
        return map.joinToString("\n") { row -> row.joinToString("") }
    }

    // Check for win condition (B is on top of X)
    private fun checkWinCondition(): Boolean {
        // Check saved goal positions
        for (goal in goalPositions) {
            val (goalX, goalY) = goal
            // If a goal doesn't have a box the game isn't over
            if (map[goalY][goalX] != 'B') {
                return false
            }
        }
        // If all goal positions have a box on top the player wins
        return true
    }
}

/*
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SokobanTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SokobanTheme {
        Greeting("Android")
    }
}*/