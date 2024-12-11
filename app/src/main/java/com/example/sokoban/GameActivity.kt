package com.example.sokoban

import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.sokoban.db.DatabaseHelper
import com.example.sokoban.db.repositories.SettingsRepository
import com.google.android.gms.games.Player

class GameActivity : AppCompatActivity() {

    private lateinit var gridGameMap: GridLayout
    private lateinit var btnUp: Button
    private lateinit var btnDown: Button
    private lateinit var btnLeft: Button
    private lateinit var btnRight: Button
    private lateinit var tvGameStatus: TextView // Game status TextView
    private lateinit var tvTimer: TextView // Timer TextView
    private lateinit var gameMap: GameMap
    private lateinit var playerMovement: PlayerMovement
    private lateinit var swipeScreen: SwipeScreen
    private lateinit var btnRestart: Button
    private lateinit var btnUndo: Button

    private var level = 1 // Track the current level

    // Timer variables
    private var startTime: Long = 0L
    private var elapsedTime: Long = 0L
    private var timerRunning = false
    private val timerHandler = Handler()


    private lateinit var sharedPreferences: SharedPreferences
    private var userId: Long = -1L // Default value indicating no user logged in

    private lateinit var player: Player  // This should be either Alien or Gnome

    private val timerRunnable = object : Runnable {
        override fun run() {
            val currentTime = System.currentTimeMillis()
            elapsedTime = currentTime - startTime
            val seconds = (elapsedTime / 1000).toInt() % 60
            val minutes = (elapsedTime / (1000 * 60)).toInt()
            tvTimer.text = String.format("%02d:%02d", minutes, seconds)
            timerHandler.postDelayed(this, 1000) // Update every second
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        userId = sharedPreferences.getLong("userId", -1L)

        if (userId == -1L) {
            Toast.makeText(this, "No user logged in!", Toast.LENGTH_SHORT).show()
            finish() // Exit activity if no user is logged in
            return
        }

        level = intent.getIntExtra("level", 1)

        // Initialize UI components
        gridGameMap = findViewById(R.id.grid_game_map)
        btnUp = findViewById(R.id.btn_up)
        btnDown = findViewById(R.id.btn_down)
        btnLeft = findViewById(R.id.btn_left)
        btnRight = findViewById(R.id.btn_right)
        tvGameStatus = findViewById(R.id.tv_game_status)
        tvTimer = findViewById(R.id.tv_timer)
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
        if (!timerRunning) {
            startTime = System.currentTimeMillis()
            timerRunning = true
            timerHandler.post(timerRunnable)
        }
        playerMovement.movePlayer(dx, dy)
        updateGameStatus()

        // Check for game over
        if (playerMovement.checkForNoMovesLeft()) {
            showGameOverDialog()
        }
    }

    private fun updateGameStatus() {
        tvGameStatus.text = "Moves: ${playerMovement.moveCount}"
    }

    private fun restartGame() {
        if (timerRunning) {
            timerHandler.removeCallbacks(timerRunnable)
            timerRunning = false
        }
        elapsedTime = 0L
        tvTimer.text = "00:00" // Reset timer display

        gameMap = GameMap(gridGameMap, assets, level)  // Reset game map
        playerMovement = PlayerMovement(this, gameMap, tvGameStatus)  // Reinitialize player movement
        gameMap.renderMap()  // Render the map again
        updateGameStatus()  // Update the status view
    }

    private fun undoMove() {
        playerMovement.undoMove()
        updateGameStatus()
    }
/**
 * Make this button here for the player to se the inventory
    val openInventoryButton = findViewById<Button>(R.id.open_inventory_button)
    openInventoryButton.setOnClickListener {
        val intent = Intent(this, InventoryActivity::class.java)
        startActivity(intent)
    }
   */
    fun showGameOverDialog() {
        if (timerRunning) {
            timerHandler.removeCallbacks(timerRunnable)
            timerRunning = false
        }

        val dbHelper = DatabaseHelper(this)
        val settingsRepository = SettingsRepository(dbHelper)

        // Retrieve saved settings for the current user
        val settings = settingsRepository.getSettings(userId)
        if (settings != null && settings.soundEnabled) {
            val masterVolume = settings.masterVolume / 100.0f
            val soundVolume = settings.soundVolume / 100.0f
            val effectiveVolume = masterVolume * soundVolume

            // Play game over sound (use a different sound for game over)
            val mediaPlayer = MediaPlayer.create(this, R.raw.game_over)
            mediaPlayer.setVolume(effectiveVolume, effectiveVolume) // Apply effective volume
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener {
                mediaPlayer.release() // Release resources
            }
        }

        AlertDialog.Builder(this)
            .setTitle("Game Over")
            .setMessage("No moves left or a critical error occurred.")
            .setPositiveButton("Retry") { dialog, _ ->
                dialog.dismiss()
                restartGame() // Restart the current level
            }
            .setNegativeButton("Back") { dialog, _ ->
                dialog.dismiss()
                goBackToMainMenu() // Return to the main menu
            }
            .show()
    }

    fun showWinDialog(moveCount: Int) {
        if (timerRunning) {
            timerHandler.removeCallbacks(timerRunnable)
            timerRunning = false
        }

        val dbHelper = DatabaseHelper(this)
        val settingsRepository = SettingsRepository(dbHelper)

        // Retrieve saved settings for the current user
        val settings = settingsRepository.getSettings(userId)
        if (settings != null && settings.soundEnabled) {
            val masterVolume = settings.masterVolume / 100.0f
            val soundVolume = settings.soundVolume / 100.0f
            val effectiveVolume = masterVolume * soundVolume

            // Play victory sound
            val mediaPlayer = MediaPlayer.create(this, R.raw.win)
            mediaPlayer.setVolume(effectiveVolume, effectiveVolume) // Apply effective volume
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener {
                mediaPlayer.release() // Release resources
            }
        }

        // Calculate total time in seconds
        val totalTimeInSeconds = (elapsedTime / 1000).toInt()

        // Get the current date as a string
        val currentDate = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date())

        // Add high score to the database
        val highScoreId = dbHelper.addHighScore(userId, level, totalTimeInSeconds, moveCount, currentDate)
        if (highScoreId != -1L) {
            Toast.makeText(this, "High score added!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Failed to add high score!", Toast.LENGTH_SHORT).show()
        }

        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("highest_level", level)  // Save the highest level the user completed
        editor.apply()

        // Show win dialog
        AlertDialog.Builder(this)
            .setTitle("You Win!")
            .setMessage("You completed the level in $moveCount moves and ${tvTimer.text} time.")
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
