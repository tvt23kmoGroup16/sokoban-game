package com.example.sokoban

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.sokoban.db.DatabaseHelper
import com.example.sokoban.db.models.User
import com.example.sokoban.databinding.ActivityLoginBinding


class LoginActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val databaseHelper = DatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Button listeners setup
        btnRegisterListener()
        btnLoginListener()
        btnForgotPasswordListener()

    }
        private fun btnLoginListener() {
            binding.btnLogin.setOnClickListener {
                val usernameOrEmail = binding.etUsername.text.toString().trim()
                val password = binding.etPassword.text.toString().trim()

                // Check user credentials
                if (checkUserCredentials(usernameOrEmail, password)) {
                    val intent = Intent(this, UserActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Invalid credentials, please try again.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun btnRegisterListener() {
        binding.btnRegister.setOnClickListener {
            // Navigate to RegisterActivity
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun btnForgotPasswordListener() {
        binding.btnForgotPassword.setOnClickListener {
            // Navigate to ForgotPasswordActivity
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    // Check user credentials in the database (username or email + password)
    private fun checkUserCredentials(usernameOrEmail: String, password: String): Boolean {
        val db = this.openOrCreateDatabase("user_db", MODE_PRIVATE, null)
        val cursor = db.query(
            "users", // Replace with your actual table name
            arrayOf("password_hash"), // Fetch only the password hash column
            "username = ? OR email = ?",
            arrayOf(usernameOrEmail, usernameOrEmail),
            null, null, null
        )

        return try {
            if (cursor.moveToFirst()) {
                // Get the stored hashed password from the database
                val storedPasswordHash = cursor.getString(
                    cursor.getColumnIndexOrThrow("password_hash") // Column name in your table
                )

                // Check if the stored hash is not null or empty
                if (storedPasswordHash.isNullOrEmpty()) {
                    false
                } else {
                    // Verify the entered password with the stored hash
                    val result = BCrypt.verifyer().verify(password.toCharArray(), storedPasswordHash)
                    result.verified
                }
            } else {
                // User not found
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            // Ensure cursor and database are closed properly
            cursor.close()
            db.close()
        }
    }
}