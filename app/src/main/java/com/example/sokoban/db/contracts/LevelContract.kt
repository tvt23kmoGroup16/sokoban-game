package com.example.sokoban.db.contracts

import android.provider.BaseColumns

object LevelContract {
    object LevelEntry : BaseColumns {
        const val TABLE_NAME = "levels"
        const val COLUMN_ID = BaseColumns._ID // Inherits ID
        const val COLUMN_LEVEL_NUMBER = "levelNumber" // e.g., 1, 2, 3, ...
        const val COLUMN_LEVEL_NAME = "levelName" // Name of the level, for example "Easter Garden"
        const val COLUMN_DIFFICULTY = "difficulty" // Difficulty rating
        const val COLUMN_REQUIRED_SCORE = "requiredScore" // Score needed to unlock level
    }
}