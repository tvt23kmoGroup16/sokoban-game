package com.example.sokoban

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException

class CharacterActivity : AppCompatActivity() {

    private lateinit var characterImages: Array<String>
    private lateinit var characterNames: Array<String>
    private var currentCharacterIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)

        // Initialize character data
        characterImages = arrayOf(
            "characters/character1.png",
            "characters/character2.png",
            "characters/player.png"
        )
        characterNames = arrayOf("Alien", "Gnome", "Lego Indy")

        val imgCharacter: ImageView = findViewById(R.id.img_character)
        val tvCharacterName: TextView = findViewById(R.id.tv_character_name)
        val btnArrowLeft: TextView = findViewById(R.id.btn_arrow_left)
        val btnArrowRight: TextView = findViewById(R.id.btn_arrow_right)
        val btnConfirmCharacter: Button = findViewById(R.id.btn_confirm_character)

        // Display the initial character
        updateCharacterDisplay(imgCharacter, tvCharacterName)

        btnArrowLeft.setOnClickListener {
            currentCharacterIndex =
                if (currentCharacterIndex > 0) currentCharacterIndex - 1 else characterImages.size - 1
            updateCharacterDisplay(imgCharacter, tvCharacterName)
        }

        btnArrowRight.setOnClickListener {
            currentCharacterIndex =
                if (currentCharacterIndex < characterImages.size - 1) currentCharacterIndex + 1 else 0
            updateCharacterDisplay(imgCharacter, tvCharacterName)
        }

        // Confirm character selection
        btnConfirmCharacter.setOnClickListener {
            // Save the selected character or pass it to another activity
            //val selectedCharacterName = characterNames[currentCharacterIndex]
            finish() // Close activity after confirming
        }
    }

    private fun updateCharacterDisplay(imgCharacter: ImageView, tvCharacterName: TextView) {
        // Load the image from assets
        try {
            val assetManager = assets
            val inputStream = assetManager.open(characterImages[currentCharacterIndex])
            val drawable = Drawable.createFromStream(inputStream, null)
            imgCharacter.setImageDrawable(drawable)
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // Update the character name
        tvCharacterName.text = characterNames[currentCharacterIndex]
    }
}
