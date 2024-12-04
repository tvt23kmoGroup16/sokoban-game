package com.example.sokoban

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LevelSelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level_selection)

        // Find buttons
        val btnContinue: Button = findViewById(R.id.btn_continue)
        val btnLevel1: Button = findViewById(R.id.btn_level_1)
        val btnLevel2: Button = findViewById(R.id.btn_level_2)
        val btnLevel3: Button = findViewById(R.id.btn_level_3)
        val btnLevel4: Button = findViewById(R.id.btn_level_4)
        val btnLevel5: Button = findViewById(R.id.btn_level_5)

        btnContinue.setOnClickListener {
            Toast.makeText(this, "Continue Pressed", Toast.LENGTH_SHORT).show()
        }

        btnLevel1.setOnClickListener {
            // Start GameActivity and pass the level
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("level", 1) // Pass level information to GameActivity
            startActivity(intent)
        }

        btnLevel2.setOnClickListener {
            // Start GameActivity and pass the level
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("level", 2) // Pass level information to GameActivity
            startActivity(intent)
        }

        btnLevel3.setOnClickListener {
            // Start GameActivity and pass the level
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("level", 3) // Pass level information to GameActivity
            startActivity(intent)
        }

        btnLevel4.setOnClickListener {
            // Start GameActivity and pass the level
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("level", 4) // Pass level information to GameActivity
            startActivity(intent)
        }

        btnLevel5.setOnClickListener {
            // Start GameActivity and pass the level
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("level", 5) // Pass level information to GameActivity
            startActivity(intent)
        }
    }
}
