package com.example.sokoban.db.contracts

import android.provider.BaseColumns

object HighScoreContract {
    object HighScoreEntry : BaseColumns {
        const val TABLE_NAME = "high_scores"
        const val COLUMN_ID = BaseColumns._ID // Inherits ID, PK
        const val COLUMN_USER_ID = "user_id" // Foreign key referencing users
        const val COLUMN_SCORE = "score" // The score achieved
        const val COLUMN_LEVEL = "level" // Level of achieved score
        const val COLUMN_DATE = "date" // Date of score
    }
}