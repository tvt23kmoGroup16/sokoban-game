package com.example.sokoban.db.repositories

import android.content.ContentValues
import com.example.sokoban.db.DatabaseHelper
import com.example.sokoban.db.contracts.LevelContract
import com.example.sokoban.db.models.Level

class LevelRepository(private val dbHelper: DatabaseHelper) {

    // Insert a new level
    fun insert(levelNumber: Int, levelName: String, difficulty: String, requiredScore: Int): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(LevelContract.LevelEntry.COLUMN_LEVEL_NUMBER, levelNumber)
            put(LevelContract.LevelEntry.COLUMN_LEVEL_NAME, levelName)
            put(LevelContract.LevelEntry.COLUMN_DIFFICULTY, difficulty)
            put(LevelContract.LevelEntry.COLUMN_REQUIRED_SCORE, requiredScore)
        }
        return db.insert(LevelContract.LevelEntry.TABLE_NAME, null, values)
    }

    // Get all levels
    fun getAll(): List<Level> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            LevelContract.LevelEntry.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            "${LevelContract.LevelEntry.COLUMN_LEVEL_NUMBER} ASC"
        )

        val levels = mutableListOf<Level>()
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(LevelContract.LevelEntry.COLUMN_ID))
            val levelNumber = cursor.getInt(cursor.getColumnIndexOrThrow(LevelContract.LevelEntry.COLUMN_LEVEL_NUMBER))
            val levelName = cursor.getString(cursor.getColumnIndexOrThrow(LevelContract.LevelEntry.COLUMN_LEVEL_NAME))
            val difficulty = cursor.getString(cursor.getColumnIndexOrThrow(LevelContract.LevelEntry.COLUMN_DIFFICULTY))
            val requiredScore = cursor.getInt(cursor.getColumnIndexOrThrow(LevelContract.LevelEntry.COLUMN_REQUIRED_SCORE))
            levels.add(Level(id, levelNumber, levelName, difficulty, requiredScore))
        }
        cursor.close()
        return levels
    }

    // Update the difficulty of a level
    fun update(id: Long, newDifficulty: String): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(LevelContract.LevelEntry.COLUMN_DIFFICULTY, newDifficulty)
        }
        return db.update(
            LevelContract.LevelEntry.TABLE_NAME,
            values,
            "${LevelContract.LevelEntry.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }
}
