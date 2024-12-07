package com.example.sokoban

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sokoban.db.DatabaseHelper

class UserActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var tvProfilePlaceholder: TextView
    private lateinit var tvUsername: TextView
    private lateinit var tvEmail: TextView
    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnSave: Button
    private lateinit var btnResetPassword: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)

        // Get references to views
        tvProfilePlaceholder = findViewById(R.id.tv_profile_placeholder)
        tvUsername = findViewById(R.id.tv_username)
        tvEmail = findViewById(R.id.tv_email)
        etUsername = findViewById(R.id.et_username)
        etEmail = findViewById(R.id.et_email)
        btnSave = findViewById(R.id.btn_save)
        btnResetPassword = findViewById(R.id.btn_reset_password)

        databaseHelper = DatabaseHelper(this)

        // Retrieve userId from SharedPreferences
        val userId = sharedPreferences.getLong("userId", -1L)
        if (userId == -1L) {
            Toast.makeText(this, "No user logged in!", Toast.LENGTH_SHORT).show()
            finish() // Exit activity if user is not logged in
            return
        }

        // Fetch user data
        val user = databaseHelper.getUserById(userId)
        if (user != null) {
            // Display current username and email
            tvUsername.text = "Username: ${user.username}"
            tvEmail.text = "Email: ${user.email}"

            // Set EditTexts with current values
            etUsername.setText(user.username)
            etEmail.setText(user.email)

            // Profile setup
            setupProfile(tvProfilePlaceholder, user.username)
        } else {
            Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show()
        }

        // Save changes button
        btnSave.setOnClickListener {
            val newUsername = etUsername.text.toString().trim()
            val newEmail = etEmail.text.toString().trim()

            if (newUsername.isNotEmpty() && newEmail.isNotEmpty()) {
                // Update the user details in the database
                val userUpdated = databaseHelper.updateUser(userId, newUsername, newEmail)
                if (userUpdated) {
                    Toast.makeText(this, "User details updated", Toast.LENGTH_SHORT).show()

                    // Update UI with new details
                    tvUsername.text = "Username: $newUsername"
                    tvEmail.text = "Email: $newEmail"

                    // Update profile circle
                    setupProfile(tvProfilePlaceholder, newUsername)
                } else {
                    Toast.makeText(this, "Failed to update details", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter valid username and email", Toast.LENGTH_SHORT).show()
            }
        }

        // Reset password button
        btnResetPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupProfile(tvProfilePlaceholder: TextView, username: String) {
        val firstLetter = username.firstOrNull()?.uppercase() ?: "?"
        tvProfilePlaceholder.text = firstLetter
        tvProfilePlaceholder.setBackgroundColor(generateColorFromName(username))
        tvProfilePlaceholder.visibility = View.VISIBLE
    }

    // Generate a color based on the username
    private fun generateColorFromName(name: String): Int {
        val hash = name.hashCode()
        val r = (hash and 0xFF0000) shr 16
        val g = (hash and 0x00FF00) shr 8
        val b = (hash and 0x0000FF)
        return Color.rgb(r, g, b)
    }
}
