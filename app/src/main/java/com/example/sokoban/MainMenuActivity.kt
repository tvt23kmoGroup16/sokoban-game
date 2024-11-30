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

        btnLogin.setOnClickListener {
            // Login action
        }

        btnPlay.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

        btnOptions.setOnClickListener {
            // Options action
        }
    }
}
