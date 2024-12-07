package com.example.sokoban.db.repositories

import android.content.ContentValues
import androidx.core.database.getStringOrNull
import com.example.sokoban.db.DatabaseHelper
import com.example.sokoban.db.contracts.UserContract
import com.example.sokoban.db.models.User

class UserRepository(private val dbHelper: DatabaseHelper) {

    // Create values
    fun insertUser(
        username: String,
        email: String,
        passwordHash: String,
        lastLogin: Long = System.currentTimeMillis(),
        score: Int = 0,
        level: Int = 1
    ): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(UserContract.UserEntry.COLUMN_USERNAME, username)
            put(UserContract.UserEntry.COLUMN_EMAIL, email)
            put(UserContract.UserEntry.COLUMN_PASSWORD_HASH, passwordHash)
            put(UserContract.UserEntry.COLUMN_LAST_LOGIN, lastLogin)
            put(UserContract.UserEntry.COLUMN_SCORE, score)
            put(UserContract.UserEntry.COLUMN_LEVEL, level)
        }
        return db.insert(UserContract.UserEntry.TABLE_NAME, null, values)
    }

    // Read values
    fun getUserById(id: Long): User? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            UserContract.UserEntry.TABLE_NAME,
            null,
            "${UserContract.UserEntry.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            val user = User(
                id = cursor.getLong(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_ID)),
                username = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_USERNAME)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_EMAIL)),
                passwordHash = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_PASSWORD_HASH)),
                lastLogin = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_LAST_LOGIN)),
                score = cursor.getInt(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_SCORE)),
                level = cursor.getInt(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_LEVEL))
            )
            cursor.close()
            user
        } else {
            cursor.close()
            null
        }
    }
    // Get all users
    fun getAllUsers(): List<User> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            UserContract.UserEntry.TABLE_NAME,
            null, // Get all columns
            null, // No selection
            null, // No selection arguments
            null, // No group by
            null, // No having
            "${UserContract.UserEntry.COLUMN_USERNAME} ASC" // Sort by username in ascending order
        )

        val users = mutableListOf<User>()
        while (cursor.moveToNext()) {
            val user = User(
                id = cursor.getLong(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_ID)),
                username = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_USERNAME)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_EMAIL)),
                passwordHash = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_PASSWORD_HASH)),
                lastLogin = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_LAST_LOGIN)),
                score = cursor.getInt(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_SCORE)),
                level = cursor.getInt(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_LEVEL))
            )
            users.add(user)
        }
        cursor.close()
        return users
    }

    // Update values
    fun updateUser(
        id: Int?,
        newUsername: String,
        newEmail: String,
        hashedPassword: String,
        lastLogin: String
    ): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(UserContract.UserEntry.COLUMN_USERNAME, newUsername)
            put(UserContract.UserEntry.COLUMN_EMAIL, newEmail)
        }
        return db.update(
            UserContract.UserEntry.TABLE_NAME,
            values,
            "${UserContract.UserEntry.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    // Delete values
    fun deleteUser(id: Long): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            UserContract.UserEntry.TABLE_NAME,
            "${UserContract.UserEntry.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    // Additional: Check if a user exists by email
    fun getUserByEmail(email: String): User? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            UserContract.UserEntry.TABLE_NAME,
            null,
            "${UserContract.UserEntry.COLUMN_EMAIL} = ?",
            arrayOf(email),
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            val user = User(
                id = cursor.getLong(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_ID)),
                username = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_USERNAME)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_EMAIL)),
                passwordHash = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_PASSWORD_HASH)),
                lastLogin = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_LAST_LOGIN)),
                score = cursor.getInt(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_SCORE)),
                level = cursor.getInt(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_LEVEL))
            )
            cursor.close()
            user
        } else {
            cursor.close()
            null
        }
    }
}

