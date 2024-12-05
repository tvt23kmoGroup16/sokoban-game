package com.example.sokoban.db.repositories

import android.content.ContentValues
import com.example.sokoban.db.DatabaseHelper
import com.example.sokoban.db.contracts.HighScoreContract
import com.example.sokoban.db.models.HighScore

class HighScoreRepository(private val dbHelper: DatabaseHelper) {

    // Insert a new high score
    fun insertHighScore(userId: Long, score: Int, level: Int, date: String): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(HighScoreContract.HighScoreEntry.COLUMN_USER_ID, userId)
            put(HighScoreContract.HighScoreEntry.COLUMN_SCORE, score)
            put(HighScoreContract.HighScoreEntry.COLUMN_LEVEL, level)
            put(HighScoreContract.HighScoreEntry.COLUMN_DATE, date)
        }
        return db.insert(HighScoreContract.HighScoreEntry.TABLE_NAME, null, values)
    }

    // Get all high scores for a specific user
    fun getHighScoresByUserId(userId: Long): List<HighScore> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            HighScoreContract.HighScoreEntry.TABLE_NAME,
            null,
            "${HighScoreContract.HighScoreEntry.COLUMN_USER_ID} = ?",
            arrayOf(userId.toString()),
            null,
            null,
            "${HighScoreContract.HighScoreEntry.COLUMN_SCORE} DESC" // Order by highest score
        )

        val highScores = mutableListOf<HighScore>()
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(HighScoreContract.HighScoreEntry.COLUMN_ID))
            val score = cursor.getInt(cursor.getColumnIndexOrThrow(HighScoreContract.HighScoreEntry.COLUMN_SCORE))
            val level = cursor.getInt(cursor.getColumnIndexOrThrow(HighScoreContract.HighScoreEntry.COLUMN_LEVEL))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(HighScoreContract.HighScoreEntry.COLUMN_DATE))
            highScores.add(HighScore(id, userId, score, level, date))
        }
        cursor.close()
        return highScores
    }

    // Update a high score
    fun updateHighScore(id: Long, newScore: Int, newLevel: Int): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(HighScoreContract.HighScoreEntry.COLUMN_SCORE, newScore)
            put(HighScoreContract.HighScoreEntry.COLUMN_LEVEL, newLevel)
        }
        return db.update(
            HighScoreContract.HighScoreEntry.TABLE_NAME,
            values,
            "${HighScoreContract.HighScoreEntry.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    // Delete a high score entry by ID
    fun deleteHighScore(id: Long): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            HighScoreContract.HighScoreEntry.TABLE_NAME,
            "${HighScoreContract.HighScoreEntry.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }
}
