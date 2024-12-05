package com.example.sokoban.db.contracts

import android.provider.BaseColumns

object SettingsContract {
        object SettingsEntry : BaseColumns {
            const val TABLE_NAME = "settings"
            const val COLUMN_ID = BaseColumns._ID // Inherits ID, this always PK
            const val COLUMN_USER_ID = "user_id" // Foreign key referencing users
            const val COLUMN_MUSIC_ENABLED = "music_enabled" // Boolean for music settings
            const val COLUMN_SOUND_ENABLED = "sound_enabled" // Boolean for sound settings
            const val COLUMN_MASTER_VOLUME = "master_volume" //Integer for master volume
            const val COLUMN_MUSIC_VOLUME = "music_volume" //Integer for music volume
            const val COLUMN_SOUND_VOLUME = "sound_volume" //Integer for sound volume
        }
}
