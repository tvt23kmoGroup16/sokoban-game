package com.example.sokoban

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.sokoban.db.DatabaseHelper
import com.example.sokoban.db.repositories.SettingsRepository

class MainMenuActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val btnLogin: Button = findViewById(R.id.btn_login)
        val btnPlay: Button = findViewById(R.id.btn_play)
        val btnOptions: Button = findViewById(R.id.btn_options)
        val btnChooseCharacter: Button = findViewById(R.id.btn_choose_character)

        initMusic()

        btnLogin.setOnClickListener {
            // Login action
        }

        btnPlay.setOnClickListener {
            // Navigate to LevelSelectionActivity
            startActivity(Intent(this, LevelSelectionActivity::class.java))
        }

        btnOptions.setOnClickListener {
            // Navigate to OptionsActivity
            startActivity(Intent(this, OptionsActivity::class.java))
        }

        btnChooseCharacter.setOnClickListener {
            // Navigate to CharacterActivity
            startActivity(Intent(this, CharacterActivity::class.java))
        }
    }

    private fun initMusic() {
        val dbHelper = DatabaseHelper(this)
        val settingsRepository = SettingsRepository(dbHelper)
        val settings = settingsRepository.getSettings(1L) // Replace 1L with the actual user ID

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

    private fun stopMusic() {
        mediaPlayer?.pause()
    }

    private fun refreshMusicSettings() {
        initMusic() // Reinitialize music with updated settings
    }

    override fun onResume() {
        super.onResume()
        refreshMusicSettings() // Refresh settings when returning to this activity
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
