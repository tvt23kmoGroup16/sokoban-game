package com.example.sokoban.db.contracts

import android.provider.BaseColumns

object HighScoreContract {
    object HighScoreEntry {
        const val TABLE_NAME = "high_scores"
        const val COLUMN_ID = "id"
        const val COLUMN_USER_ID = "user_id"
        const val COLUMN_LEVEL = "level"
        const val COLUMN_TIME = "time"
        const val COLUMN_MOVES = "moves"
        const val COLUMN_DATE = "date"
    }
}
