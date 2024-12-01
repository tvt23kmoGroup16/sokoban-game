package com.example.sokoban

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {

    private lateinit var gridGameMap: GridLayout
    private lateinit var btnUp: Button
    private lateinit var btnDown: Button
    private lateinit var btnLeft: Button
    private lateinit var btnRight: Button
    private lateinit var tvGameStatus: TextView // Game status TextView
    private lateinit var gameMap: GameMap
    private lateinit var playerMovement: PlayerMovement
    private lateinit var swipeScreen: SwipeScreen
    private lateinit var btnRestart: Button
    private lateinit var btnUndo: Button

    private var level = 1 // Track the current level

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        level = intent.getIntExtra("level", 1)

        // Initialize UI components
        gridGameMap = findViewById(R.id.grid_game_map)
        btnUp = findViewById(R.id.btn_up)
        btnDown = findViewById(R.id.btn_down)
        btnLeft = findViewById(R.id.btn_left)
        btnRight = findViewById(R.id.btn_right)
        tvGameStatus = findViewById(R.id.tv_game_status)
        btnRestart = findViewById(R.id.btn_restart)
        btnUndo = findViewById(R.id.btn_undo)

        // Initialize game map with the correct level
        gameMap = GameMap(gridGameMap, assets, level)
        playerMovement = PlayerMovement(this, gameMap, tvGameStatus)

        // Render map
        gameMap.renderMap()

        // Button-based movement
        btnUp.setOnClickListener { movePlayerAndUpdate(0, -1) }
        btnDown.setOnClickListener { movePlayerAndUpdate(0, 1) }
        btnLeft.setOnClickListener { movePlayerAndUpdate(-1, 0) }
        btnRight.setOnClickListener { movePlayerAndUpdate(1, 0) }

        btnRestart.setOnClickListener { restartGame() }
        btnUndo.setOnClickListener { undoMove() }

        // Swipe detection
        swipeScreen = SwipeScreen(this, object : SwipeScreen.SwipeCallback {
            override fun onSwipeUp() { movePlayerAndUpdate(0, -1) }
            override fun onSwipeDown() { movePlayerAndUpdate(0, 1) }
            override fun onSwipeLeft() { movePlayerAndUpdate(-1, 0) }
            override fun onSwipeRight() { movePlayerAndUpdate(1, 0) }
        })
        val rootView = findViewById<View>(android.R.id.content)
        rootView.setOnTouchListener(swipeScreen)
    }

    private fun movePlayerAndUpdate(dx: Int, dy: Int) {
        playerMovement.movePlayer(dx, dy)
        updateGameStatus()
    }

    private fun updateGameStatus() {
        tvGameStatus.text = "Moves: ${playerMovement.moveCount}"
    }

    private fun restartGame() {
        // Reset to the current level
        gameMap = GameMap(gridGameMap, assets, level)  // Reset game map
        playerMovement = PlayerMovement(this, gameMap, tvGameStatus)  // Reinitialize player movement
        gameMap.renderMap()  // Render the map again
        updateGameStatus()  // Update the status view
    }



    private fun undoMove() {
        playerMovement.undoMove()
        updateGameStatus()
    }

    fun showWinDialog(moveCount: Int) {
        AlertDialog.Builder(this)
            .setTitle("You Win!")
            .setMessage("You completed the level in $moveCount moves.")
            .setPositiveButton("Continue") { dialog, _ ->
                dialog.dismiss()
                continueGame()    // Move to the next level
            }
            .setNegativeButton("Back") { dialog, _ ->
                dialog.dismiss()
                goBackToMainMenu()  // Navigate to the main menu
            }
            .show()
    }

    private fun continueGame() {
        // Move to the next level
        val nextLevel = level + 1  // Increment the level
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("level", nextLevel)  // Pass the next level to GameActivity
        startActivity(intent)
        finish()
    }

    private fun goBackToMainMenu() {
        // Go back to main menu
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
        finish()
    }


}
