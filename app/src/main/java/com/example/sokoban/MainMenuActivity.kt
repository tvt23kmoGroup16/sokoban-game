package com.example.sokoban

import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sokoban.db.DatabaseHelper
import com.example.sokoban.db.repositories.SettingsRepository
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar

class MainMenuActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)


        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)

        val btnLogin: Button = findViewById(R.id.btn_login)
        val btnPlay: Button = findViewById(R.id.btn_play)
        val btnOptions: Button = findViewById(R.id.btn_options)
        val btnChooseCharacter: Button = findViewById(R.id.btn_choose_character)
        val tvProfileCircle: TextView = findViewById(R.id.tv_profile_circle)
        val btnLeaderboard: Button = findViewById(R.id.btn_leaderboard)

        initMusic()
        updateLoginButton(btnLogin)
        updateProfileCircle(tvProfileCircle)

        //Login/Logout Button logic
        btnLogin.setOnClickListener {
            val userId = sharedPreferences.getLong("userId", -1L)
            if (userId == -1L) {
                // Not logged in go to LoginActivity
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                //Logged in log out the user
                logoutUser(btnLogin)
                updateProfileCircle(tvProfileCircle)
            }
        }

        btnPlay.setOnClickListener {
            //Navigate to LevelSelectionActivity
            startActivity(Intent(this, LevelSelectionActivity::class.java))
        }

        btnOptions.setOnClickListener {
            //Navigate to OptionsActivity
            startActivity(Intent(this, OptionsActivity::class.java))
        }

        btnChooseCharacter.setOnClickListener {
            //Navigate to CharacterActivity
            startActivity(Intent(this, CharacterActivity::class.java))
        }

        tvProfileCircle.setOnClickListener {
            //Navigate to UserActivity
            startActivity(Intent(this, UserActivity::class.java))
        }

        btnLeaderboard.setOnClickListener {
            //Navigate to LeaderboardActivity
            startActivity(Intent(this, LeaderboardActivity::class.java))
        }
    }
    
        private fun updateProfileCircle(tvProfileCircle: TextView) {
        val userId = sharedPreferences.getLong("userId", -1L)
        if (userId != -1L) {
            // Set profile circle with the first letter of the username
            val user = DatabaseHelper(this).getUserById(userId)
            val username = user?.username ?: "?"
            val firstLetter = username.firstOrNull()?.uppercase() ?: "?"
            tvProfileCircle.text = firstLetter
            tvProfileCircle.visibility = View.VISIBLE // Show if logged in
        } else {
            tvProfileCircle.visibility = View.GONE // Hide it if not logged in
        }
    }

    // Initialize music settings based on user preferences or settings
    private fun initMusic() {
        val dbHelper = DatabaseHelper(this)
        val settingsRepository = SettingsRepository(dbHelper)
        val userId = sharedPreferences.getLong("userId", -1L)

        if (userId != -1L) {
            val settings = settingsRepository.getSettings(userId)
            if (settings != null && settings.musicEnabled) {
                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer.create(this, R.raw.ducktales)
                    mediaPlayer?.isLooping = true
                }

                val musicVolume = (settings.masterVolume / 100.0 * settings.musicVolume / 100.0).toFloat()
                mediaPlayer?.setVolume(musicVolume, musicVolume)
                mediaPlayer?.start()
            } else {
                stopMusic()
            }
        }
    }

    private fun stopMusic() {
        mediaPlayer?.pause()
    }

    private fun refreshMusicSettings() {
        initMusic()
    }

    override fun onResume() {
        super.onResume()
        refreshMusicSettings()
        val btnLogin: Button = findViewById(R.id.btn_login)
        updateLoginButton(btnLogin)

        // Update the profile circle visibility and text again when resuming the activity
        val tvProfileCircle: TextView = findViewById(R.id.tv_profile_circle)
        updateProfileCircle(tvProfileCircle)
    }

    private fun updateLoginButton(btnLogin: Button) {
        val userId = sharedPreferences.getLong("userId", -1L)
        if (userId == -1L) {
            btnLogin.text = "Log In"
        } else {
            btnLogin.text = "Log Out"
        }
    }

    private fun logoutUser(btnLogin: Button) {
        val editor = sharedPreferences.edit()
        editor.remove("userId")
        editor.apply()
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()

        updateLoginButton(btnLogin)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}


