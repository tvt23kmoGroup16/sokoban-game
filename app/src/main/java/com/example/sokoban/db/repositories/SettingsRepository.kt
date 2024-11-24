package com.example.sqlite_playground.db.repositories

import android.content.ContentValues
import com.example.sqlite_playground.db.DatabaseHelper
import com.example.sqlite_playground.db.contracts.SettingsContract
import com.example.sqlite_playground.db.models.Settings

class SettingsRepository(private val dbHelper: DatabaseHelper) {

    // Insert a new settings row
    fun insertSettings(userId: Long, musicEnabled: Boolean): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(SettingsContract.SettingsEntry.COLUMN_USER_ID, userId)
            put(SettingsContract.SettingsEntry.COLUMN_MUSIC_ENABLED, if (musicEnabled) 1 else 0) // Convert Boolean to Integer
        }
        return db.insert(SettingsContract.SettingsEntry.TABLE_NAME, null, values)
    }

    // Retrieve settings for a specific user
    fun getSettingsByUserId(userId: Long): Settings? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            SettingsContract.SettingsEntry.TABLE_NAME,
            null,
            "${SettingsContract.SettingsEntry.COLUMN_USER_ID} = ?",
            arrayOf(userId.toString()),
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(SettingsContract.SettingsEntry.COLUMN_ID))
            val musicEnabled = cursor.getInt(cursor.getColumnIndexOrThrow(SettingsContract.SettingsEntry.COLUMN_MUSIC_ENABLED)) == 1
            cursor.close()
            Settings(id, userId, musicEnabled) // Converting Integer to Boolean true/false
        } else {
            cursor.close()
            null
        }
    }

    // Update musicEnabled value for a user
    fun updateMusicEnabled(userId: Long, musicEnabled: Boolean): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(SettingsContract.SettingsEntry.COLUMN_MUSIC_ENABLED, if (musicEnabled) 1 else 0) // Convert Boolean to Integer
        }
        return db.update(
            SettingsContract.SettingsEntry.TABLE_NAME,
            values,
            "${SettingsContract.SettingsEntry.COLUMN_USER_ID} = ?",
            arrayOf(userId.toString())
        )
    }

    // Delete settings for a user
    fun deleteSettingsByUserId(userId: Long): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            SettingsContract.SettingsEntry.TABLE_NAME,
            "${SettingsContract.SettingsEntry.COLUMN_USER_ID} = ?",
            arrayOf(userId.toString())
        )
    }
}
