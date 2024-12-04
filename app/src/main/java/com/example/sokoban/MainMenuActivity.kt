package com.example.sokoban

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val btnLogin: Button = findViewById(R.id.btn_login)
        val btnPlay: Button = findViewById(R.id.btn_play)
        val btnOptions: Button = findViewById(R.id.btn_options)
        val btnChooseCharacter: Button = findViewById(R.id.btn_choose_character)

        btnLogin.setOnClickListener {
            // Login action
        }

        btnPlay.setOnClickListener {
            // Navigate to LevelSelectionActivity
            val intent = Intent(this, LevelSelectionActivity::class.java)
            startActivity(intent)
        }

        btnOptions.setOnClickListener {
            // Navigate to OptionsActivity
            val intent = Intent(this, OptionsActivity::class.java)
            startActivity(intent)
        }

        btnChooseCharacter.setOnClickListener {
            // Navigate to CharacterActivity
            val intent = Intent(this, CharacterActivity::class.java)
            startActivity(intent)
        }
    }
}
