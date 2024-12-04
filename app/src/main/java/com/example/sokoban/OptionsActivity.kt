package com.example.sokoban

import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class OptionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)

        val seekbarMasterVolume: SeekBar = findViewById(R.id.seekbar_master_volume)
        val seekbarMusicVolume: SeekBar = findViewById(R.id.seekbar_music_volume)
        val seekbarSoundEffectsVolume: SeekBar = findViewById(R.id.seekbar_sound_effects_volume)
        val btnSave: Button = findViewById(R.id.btn_save)

        // Default values
        seekbarMasterVolume.progress = 50
        seekbarMusicVolume.progress = 50
        seekbarSoundEffectsVolume.progress = 50

        // Save button click listener
        btnSave.setOnClickListener {
            val masterVolume = seekbarMasterVolume.progress
            val musicVolume = seekbarMusicVolume.progress
            val soundEffectsVolume = seekbarSoundEffectsVolume.progress

            Toast.makeText(this, "Settings Saved!", Toast.LENGTH_SHORT).show()
            // Close the activity or navigate back
            finish()
        }
    }
}
