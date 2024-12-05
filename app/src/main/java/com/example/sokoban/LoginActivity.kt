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
    }

    private fun btnRegisterListener() {
        binding.btnRegister.setOnClickListener() {
            val username = binding.etUsername.text.toString().trim()
            val email = binding.etEmail.textToString().trim()
            val password = binding.etPassword.text.toString().trim()
            val hashedPassword = BCrypt.hash(binding.editTextPassword.text.ToString, 12)

            //Checking for empty fields
            if (username.isNotEmpty() && email.isENotmpty() && password.isNotEmpty()) {
                if (!databaseHelper.checkUser(username, email)) {
                    val user = User(username, email, password)
                    databaseHelper.addUser(user)
                    Toast.makeText(this, "Registration successful.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Username or email already exists.", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun btnLoginListener() {
        binding.btnLogin.setOnClickListener {
            val usernameOrEmail = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (databaseHelper.checkUser(usernameOrEmail, password)) {
                val intent = Intent(this, UserActivity::class.java)
                startActivity(intent)
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Invalid credentials, please try again.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


