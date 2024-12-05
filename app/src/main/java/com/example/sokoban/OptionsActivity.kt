package com.example.sokoban

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sokoban.db.DatabaseHelper
import com.example.sokoban.db.models.Settings
import com.example.sokoban.db.repositories.SettingsRepository

class OptionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)

        val seekbarMasterVolume: SeekBar = findViewById(R.id.seekbar_master_volume)
        val seekbarMusicVolume: SeekBar = findViewById(R.id.seekbar_music_volume)
        val seekbarSoundEffectsVolume: SeekBar = findViewById(R.id.seekbar_sound_effects_volume)
        val btnSave: Button = findViewById(R.id.btn_save)
        val cbMusic: CheckBox = findViewById(R.id.checkbox_music)
        val cbSound: CheckBox = findViewById(R.id.checkbox_sound_effects)


        // Default values
        seekbarMasterVolume.progress = 50
        seekbarMusicVolume.progress = 50
        seekbarSoundEffectsVolume.progress = 50

        val dbHelper = DatabaseHelper(this)
        val settingsRepository = SettingsRepository(dbHelper)

        val settings = settingsRepository.getSettings(1L) // Replace 1L with the actual user ID

        if (settings != null) {
            cbMusic.isChecked = settings.musicEnabled
            cbSound.isChecked = settings.soundEnabled
            seekbarMasterVolume.progress = settings.masterVolume
            seekbarMusicVolume.progress = settings.musicVolume
            seekbarSoundEffectsVolume.progress = settings.soundVolume
        }

        btnSave.setOnClickListener {
            val dbHelper = DatabaseHelper(this)
            val db = dbHelper.writableDatabase

            val newSettings = Settings(
                id = 0L, // This will be auto-generated
                userId = 1L, //<-- Replace with the actual user ID
                musicEnabled = cbMusic.isChecked, // Checkbox for music
                soundEnabled = cbSound.isChecked, // Checkbox for sound
                masterVolume = seekbarMasterVolume.progress, // Master volume progress
                musicVolume = seekbarMusicVolume.progress, // Music volume progress
                soundVolume = seekbarSoundEffectsVolume.progress // Sound volume progress
            )

            settingsRepository.saveSettings(newSettings)
            Toast.makeText(this, "Settings saved!", Toast.LENGTH_SHORT).show()
        }
    }
}
