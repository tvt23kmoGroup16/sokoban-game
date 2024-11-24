package com.example.sqlite_playground.db.contracts

import android.provider.BaseColumns

object SettingsContract {
        object SettingsEntry : BaseColumns {
            const val TABLE_NAME = "settings"
            const val COLUMN_ID = BaseColumns._ID // Inherits ID, this always PK
            const val COLUMN_USER_ID = "user_id" // Foreign key referencing users
            const val COLUMN_MUSIC_ENABLED = "music_enabled" // Boolean for music settings
        }
}
