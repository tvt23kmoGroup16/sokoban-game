package com.example.sokoban.db.repositories

import android.content.ContentValues
import com.example.sokoban.db.DatabaseHelper
import com.example.sokoban.db.contracts.SettingsContract
import com.example.sokoban.db.models.Settings

class SettingsRepository(private val dbHelper: DatabaseHelper) {

    // Save settings (Insert or Update based on existence)
    fun saveSettings(settings: Settings) {
        val db = dbHelper.writableDatabase
        val existingSettings = getSettings(settings.userId)

        val contentValues = ContentValues().apply {
            put(SettingsContract.SettingsEntry.COLUMN_USER_ID, settings.userId)
            put(SettingsContract.SettingsEntry.COLUMN_MUSIC_ENABLED, if (settings.musicEnabled) 1 else 0)
            put(SettingsContract.SettingsEntry.COLUMN_SOUND_ENABLED, if (settings.soundEnabled) 1 else 0)
            put(SettingsContract.SettingsEntry.COLUMN_MASTER_VOLUME, settings.masterVolume)
            put(SettingsContract.SettingsEntry.COLUMN_MUSIC_VOLUME, settings.musicVolume)
            put(SettingsContract.SettingsEntry.COLUMN_SOUND_VOLUME, settings.soundVolume)
        }

        if (existingSettings == null) {
            db.insert(SettingsContract.SettingsEntry.TABLE_NAME, null, contentValues)
        } else {
            db.update(
                SettingsContract.SettingsEntry.TABLE_NAME,
                contentValues,
                "${SettingsContract.SettingsEntry.COLUMN_USER_ID} = ?",
                arrayOf(settings.userId.toString())
            )
        }
    }

    // Retrieve settings for a specific user
    fun getSettings(userId: Long): Settings? {
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
            val soundEnabled = cursor.getInt(cursor.getColumnIndexOrThrow(SettingsContract.SettingsEntry.COLUMN_SOUND_ENABLED)) == 1
            val masterVolume = cursor.getInt(cursor.getColumnIndexOrThrow(SettingsContract.SettingsEntry.COLUMN_MASTER_VOLUME))
            val musicVolume = cursor.getInt(cursor.getColumnIndexOrThrow(SettingsContract.SettingsEntry.COLUMN_MUSIC_VOLUME))
            val soundVolume = cursor.getInt(cursor.getColumnIndexOrThrow(SettingsContract.SettingsEntry.COLUMN_SOUND_VOLUME))

            cursor.close()
            Settings(id, userId, musicEnabled, soundEnabled, masterVolume, musicVolume, soundVolume)
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
