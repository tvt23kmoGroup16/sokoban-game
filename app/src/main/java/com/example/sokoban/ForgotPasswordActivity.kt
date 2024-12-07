package com.example.sokoban

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.sokoban.databinding.ActivityForgotPasswordBinding
import com.example.sokoban.db.DatabaseHelper

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private val databaseHelper by lazy { DatabaseHelper(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnResetPassword.setOnClickListener {
            val usernameOrEmail = binding.etUsernameOrEmail.text.toString().trim()

            // Validate input field
            if (usernameOrEmail.isEmpty()) {
                Toast.makeText(this, "Please enter your username or email.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check if user exists by either username or email
            val userExists = databaseHelper.checkUser(username = usernameOrEmail, email = usernameOrEmail)

            if (userExists) {
                //Proceed with the password reset
                //Prompt user for a new password
                showNewPasswordDialog(usernameOrEmail)
            } else {
                Toast.makeText(this, "User not found.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Prompt user to enter a new password
    private fun showNewPasswordDialog(usernameOrEmail: String) {
        val newPassword = binding.etNewPassword.text.toString().trim()

        if (newPassword.isEmpty()) {
            Toast.makeText(this, "Please enter a new password.", Toast.LENGTH_SHORT).show()
            return
        }
        //Hashing the new password
        val hashedPassword = BCrypt.withDefaults().hashToString(12, newPassword.toCharArray())

        //Updating the password in the database
        val isUpdated = databaseHelper.updatePassword(usernameOrEmail, hashedPassword)
        if (isUpdated) {
            Toast.makeText(this, "Password reset successfully.", Toast.LENGTH_SHORT).show()
            finish() // Close the activity and return to the previous screen
        } else {
            Toast.makeText(this, "Password reset failed. Try again.", Toast.LENGTH_SHORT).show()
        }
    }
}
