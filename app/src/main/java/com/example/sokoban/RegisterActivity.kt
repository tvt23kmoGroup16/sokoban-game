package com.example.sokoban

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.compose.ui.text.intl.Locale
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.sokoban.databinding.ActivityRegisterBinding
import com.example.sokoban.db.DatabaseHelper
import com.example.sokoban.db.models.User
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val databaseHelper by lazy { DatabaseHelper(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up listeners
        binding.btnRegister.setOnClickListener {
            registerUser()
        }

        btnAlreadyHaveAccountListener()  // Set up the listener for the "Already have an account?" button
    }

    private fun registerUser() {
        val username = binding.etUsername.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        // Validate fields
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if user already exists
        if (databaseHelper.checkUser(username, email)) {
            Toast.makeText(this, "Username or email already exists.", Toast.LENGTH_SHORT).show()
            return
        }
        if (password.length < 8) {
            Toast.makeText(this, "Password must be at least 8 characters.", Toast.LENGTH_SHORT).show()
            return
        }
        // Hash the password before storing it
        val passwordHash = BCrypt.withDefaults().hashToString(12, password.toCharArray())

        // Get the current date and time
        val currentDate = Long
        val lastLogin: Long = 1638240000000L
        val lastLoginString: String = lastLogin.toString() // Convert Long to String

        // Create the user object
        val user = User(
            id = null,  // Let the database auto-generate the ID
            username = username,
            email = email,
            passwordHash = passwordHash,
            score = 0,  // Default score
            level = 1,  // Default level
            lastLogin = currentDate.toString()  // Set current date and time as last login
        )

        // Add the user to the database
        val result = databaseHelper.addUser(user)

        // Show success or failure message
        if (result > 0) {
            Toast.makeText(this, "Registration successful.", Toast.LENGTH_SHORT).show()
            finish() // Close the activity
        } else {
            Toast.makeText(this, "Registration failed. Try again.", Toast.LENGTH_SHORT).show()
        }
    }

    // Set up the "Already have an account?" button listener
    private fun btnAlreadyHaveAccountListener() {
        binding.btnAlreadyHaveAccount.setOnClickListener {
            // Navigate to LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Optional: Close RegisterActivity
        }
    }
}
