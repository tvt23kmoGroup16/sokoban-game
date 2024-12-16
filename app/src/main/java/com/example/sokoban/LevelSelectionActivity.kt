package com.example.sokoban

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

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

        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val highestLevel = sharedPreferences.getInt("highest_level", 0)  // Get the highest level completed

        btnContinue.setOnClickListener {
            if (highestLevel == 5) {
                // Game is completed; show a message
                Toast.makeText(this, "You've completed all levels!", Toast.LENGTH_SHORT).show()
            } else if (highestLevel > 0) {
                // Start the game from the next level
                val nextLevel = highestLevel + 1
                val intent = Intent(this, GameActivity::class.java)
                intent.putExtra("level", nextLevel)
                startActivity(intent)
            } else {
                Toast.makeText(this, "No previous level completed!", Toast.LENGTH_SHORT).show()
            }
        }

        // Set up the level buttons to start specific levels
        btnLevel1.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("level", 1)
            startActivity(intent)
        }
        btnLevel2.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("level", 2)
            startActivity(intent)
        }
        btnLevel3.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("level", 3)
            startActivity(intent)
        }
        btnLevel4.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("level", 4)
            startActivity(intent)
        }
        btnLevel5.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("level", 5)
            startActivity(intent)
        }
    }
}
