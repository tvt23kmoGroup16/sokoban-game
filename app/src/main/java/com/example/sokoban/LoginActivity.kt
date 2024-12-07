package com.example.sokoban

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.sokoban.databinding.ActivityLoginBinding
import com.example.sokoban.db.DatabaseHelper

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

        // Initialize SharedPreferences to store user session
        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)

        btnRegisterListener()
        btnLoginListener()
        btnForgotPasswordListener()
    }

    private fun btnLoginListener() {
        binding.btnLogin.setOnClickListener {
            val usernameOrEmail = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            // Check user credentials using DatabaseHelper
            if (databaseHelper.checkUserCredentials(usernameOrEmail, password)) {
                // User credentials are valid now retrieve user ID
                val userId = databaseHelper.getUserIdByUsernameOrEmail(usernameOrEmail)

                if (userId != null) {
                    // Save userId to SharedPreferences
                    val editor = sharedPreferences.edit()
                    editor.putLong("userId", userId)
                    editor.apply()

                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, UserActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Invalid credentials, please try again.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun btnRegisterListener() {
        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun btnForgotPasswordListener() {
        binding.btnForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }
}
