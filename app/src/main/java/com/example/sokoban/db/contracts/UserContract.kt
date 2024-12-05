package com.example.sokoban.db.contracts

import android.provider.BaseColumns

object UserContract {

    object UserEntry : BaseColumns {
        const val TABLE_NAME = "users"
        const val COLUMN_ID = BaseColumns._ID //Inherits ID
        const val COLUMN_USERNAME = "user_name"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PASSWORD_HASH = "password_hash"
        const val COLUMN_LAST_LOGIN = "last_login"
        const val COLUMN_SCORE = "score"
        const val COLUMN_LEVEL = "level"
    }
}