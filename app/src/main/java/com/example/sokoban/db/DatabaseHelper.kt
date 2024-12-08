package com.example.sokoban.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.sokoban.db.contracts.UserContract
import com.example.sokoban.db.contracts.LevelContract
import com.example.sokoban.db.contracts.SettingsContract
import com.example.sokoban.db.contracts.HighScoreContract
import android.database.Cursor
import at.favre.lib.crypto.bcrypt.BCrypt

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "UserDatabase.db"
        const val DATABASE_VERSION = 2 //version control
        const val TAG = "DatabaseHelper"
    }

    //Creating the users table using SQL statements:
    private val sqlCreateUsersTable =
        "CREATE TABLE ${UserContract.UserEntry.TABLE_NAME} (" +
                "${UserContract.UserEntry.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${UserContract.UserEntry.COLUMN_USERNAME} TEXT NOT NULL," +
                "${UserContract.UserEntry.COLUMN_EMAIL} TEXT NOT NULL UNIQUE," +
                "${UserContract.UserEntry.COLUMN_PASSWORD_HASH} TEXT NOT NULL," +
                "${UserContract.UserEntry.COLUMN_LAST_LOGIN} DATETIME," +
                "${UserContract.UserEntry.COLUMN_SCORE} INTEGER," +
                "${UserContract.UserEntry.COLUMN_LEVEL} INTEGER)"

    // Other tables here accordingly

    private val sqlCreateLevelsTable =
        "CREATE TABLE ${LevelContract.LevelEntry.TABLE_NAME} (" +
                "${LevelContract.LevelEntry.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${LevelContract.LevelEntry.COLUMN_LEVEL_NUMBER} INTEGER NOT NULL," +
                "${LevelContract.LevelEntry.COLUMN_LEVEL_NAME} TEXT NOT NULL UNIQUE," +
                "${LevelContract.LevelEntry.COLUMN_DIFFICULTY} STRING NOT NULL," +
                "${LevelContract.LevelEntry.COLUMN_REQUIRED_SCORE} INTEGER NOT NULL)"

    private val sqlCreateSettingsTable =
        "CREATE TABLE ${SettingsContract.SettingsEntry.TABLE_NAME} (" +
                "${SettingsContract.SettingsEntry.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${SettingsContract.SettingsEntry.COLUMN_USER_ID} INTEGER NOT NULL," +
                "${SettingsContract.SettingsEntry.COLUMN_MUSIC_ENABLED} INTEGER NOT NULL," +
                "${SettingsContract.SettingsEntry.COLUMN_SOUND_ENABLED} INTEGER NOT NULL," +
                "${SettingsContract.SettingsEntry.COLUMN_MASTER_VOLUME} INTEGER NOT NULL," +
                "${SettingsContract.SettingsEntry.COLUMN_MUSIC_VOLUME} INTEGER NOT NULL," +
                "${SettingsContract.SettingsEntry.COLUMN_SOUND_VOLUME} INTEGER NOT NULL," +
                "FOREIGN KEY (${SettingsContract.SettingsEntry.COLUMN_USER_ID}) " +
                "REFERENCES ${UserContract.UserEntry.TABLE_NAME} (${UserContract.UserEntry.COLUMN_ID}))"


    private val sqlCreateHighScoresTable =
        "CREATE TABLE ${HighScoreContract.HighScoreEntry.TABLE_NAME} (" +
                "${HighScoreContract.HighScoreEntry.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${HighScoreContract.HighScoreEntry.COLUMN_USER_ID} INTEGER NOT NULL," +
                "${HighScoreContract.HighScoreEntry.COLUMN_LEVEL} INTEGER NOT NULL," +
                "${HighScoreContract.HighScoreEntry.COLUMN_TIME} INTEGER NOT NULL," +
                "${HighScoreContract.HighScoreEntry.COLUMN_MOVES} INTEGER NOT NULL," +
                "${HighScoreContract.HighScoreEntry.COLUMN_DATE} DATETIME NOT NULL," +
                "FOREIGN KEY (${HighScoreContract.HighScoreEntry.COLUMN_USER_ID}) " +
                "REFERENCES ${UserContract.UserEntry.TABLE_NAME} (${UserContract.UserEntry.COLUMN_ID}))"



    // Deleting the users table
    private val sqlDeleteUsersTable =
        "DROP TABLE IF EXISTS ${UserContract.UserEntry.TABLE_NAME}"

    //Other deletions here...
    private val sqlDeleteLevelsTable =
        "DROP TABLE IF EXISTS ${LevelContract.LevelEntry.TABLE_NAME}"
    private val sqlDeleteSettingsTable =
        "DROP TABLE IF EXISTS ${SettingsContract.SettingsEntry.TABLE_NAME}"
    private val sqlDeleteHighScoresTable =
        "DROP TABLE IF EXISTS ${HighScoreContract.HighScoreEntry.TABLE_NAME}"

    data class User(
        val id: Long,
        val username: String,
        val email: String,
        val passwordHash: String,
        val lastLogin: String?,
        val score: Int?,
        val level: Long
    )

    data class Level(
        val id: Long,
        val levelNumber: Int,
        val levelName: String,
        val difficulty: Int,
        val requiredScore: Int
    )

    // Creating tables
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(sqlCreateUsersTable) // Creating the users table
        // other table creations here..
        db.execSQL(sqlCreateLevelsTable) // Creating the levels table
        db.execSQL(sqlCreateSettingsTable) // Creating the settings table
        db.execSQL(sqlCreateHighScoresTable) // Creating the highScores table
    }

    //Version control: upgrading tables
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            if (oldVersion < 2) { // Assuming version 2 introduces the new columns
                db.execSQL("ALTER TABLE ${HighScoreContract.HighScoreEntry.TABLE_NAME} ADD COLUMN ${HighScoreContract.HighScoreEntry.COLUMN_TIME} INTEGER NOT NULL DEFAULT 0;")
                db.execSQL("ALTER TABLE ${HighScoreContract.HighScoreEntry.TABLE_NAME} ADD COLUMN ${HighScoreContract.HighScoreEntry.COLUMN_MOVES} INTEGER NOT NULL DEFAULT 0;")
            }

        db.execSQL(sqlDeleteUsersTable) // Deleting the old users table

        //other delete old versions here...
        db.execSQL(sqlDeleteLevelsTable)
        db.execSQL(sqlDeleteSettingsTable)
        db.execSQL(sqlDeleteHighScoresTable)


        onCreate(db) // Recreating all tables
    }

    fun Cursor.getStringOrNull(columnIndex: Int): String? =
        if (isNull(columnIndex)) null else getString(columnIndex)

    fun Cursor.getIntOrNull(columnIndex: Int): Int? =
        if (isNull(columnIndex)) null else getInt(columnIndex)

    fun Cursor.isNull(columnIndex: Int): Boolean =
        getType(columnIndex) == Cursor.FIELD_TYPE_NULL


    /*deletes databases but does not reset auto-increments,
    if you want to delete auto-increments, drop tables and recreate them and do it inside this function*/
    fun resetDatabase() {
        val db = this.writableDatabase
        db.beginTransaction()
        try {
            //Clear all data
            db.delete(UserContract.UserEntry.TABLE_NAME, null, null)
            db.delete(LevelContract.LevelEntry.TABLE_NAME, null, null)
            db.delete(HighScoreContract.HighScoreEntry.TABLE_NAME, null, null)
            db.delete(SettingsContract.SettingsEntry.TABLE_NAME, null, null)

            //Resetting auto-increment counters
            db.execSQL("DELETE FROM sqlite_sequence WHERE name='${UserContract.UserEntry.TABLE_NAME}'")
            db.execSQL("DELETE FROM sqlite_sequence WHERE name='${LevelContract.LevelEntry.TABLE_NAME}'")
            db.execSQL("DELETE FROM sqlite_sequence WHERE name='${HighScoreContract.HighScoreEntry.TABLE_NAME}'")
            db.execSQL("DELETE FROM sqlite_sequence WHERE name='${SettingsContract.SettingsEntry.TABLE_NAME}'")

            db.setTransactionSuccessful() //Commit if all succeeded
            Log.d(TAG, "Database reset: Data cleared... ...auto-increment succeeded.")
        } catch (e: Exception) {
            Log.e(TAG, "Error resetting database: ", e)
        } finally {
            db.endTransaction() //Commit or rollback
            db.close()
        }
    }

    //Login and Registration handling
    // Check if the user exists by username or email
    fun checkUser(username: String?, email: String?): Boolean {
        val db = this.readableDatabase
        val selectionArgs = mutableListOf<String>()
        val conditions = mutableListOf<String>()

        // If username is provided
        if (!username.isNullOrEmpty()) {
            conditions.add("${UserContract.UserEntry.COLUMN_USERNAME} = ?")
            selectionArgs.add(username)
        }

        // If email is provided
        if (!email.isNullOrEmpty()) {
            conditions.add("${UserContract.UserEntry.COLUMN_EMAIL} = ?")
            selectionArgs.add(email)
        }

        // Combine conditions with OR if both username and email are provided
        val selection = conditions.joinToString(" OR ")

        val cursor: Cursor = db.query(
            UserContract.UserEntry.TABLE_NAME,
            null,
            selection,
            selectionArgs.toTypedArray(),
            null,
            null,
            null
        )
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    //Checking user credentials during login (username or email + password)
    fun checkUserCredentials(usernameOrEmail: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(
            UserContract.UserEntry.TABLE_NAME,
            arrayOf(UserContract.UserEntry.COLUMN_PASSWORD_HASH),
            "${UserContract.UserEntry.COLUMN_USERNAME} = ? OR ${UserContract.UserEntry.COLUMN_EMAIL} = ?",
            arrayOf(usernameOrEmail, usernameOrEmail),
            null, null, null
        )

        return try {
            if (cursor.moveToFirst()) {
                val storedPasswordHash = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_PASSWORD_HASH))
                val result = BCrypt.verifyer().verify(password.toCharArray(), storedPasswordHash)
                result.verified // true if the password is correct, false if not
            } else {
                false // user not found
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error checking user credentials", e)
            false
        } finally {
            cursor.close()
            db.close()
        }
    }

    fun updatePassword(usernameOrEmail: String, hashedPassword: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(UserContract.UserEntry.COLUMN_PASSWORD_HASH, hashedPassword)
        }

        val whereClause = "${UserContract.UserEntry.COLUMN_USERNAME} = ? OR ${UserContract.UserEntry.COLUMN_EMAIL} = ?"
        val whereArgs = arrayOf(usernameOrEmail, usernameOrEmail)

        val rowsUpdated = db.update(UserContract.UserEntry.TABLE_NAME, values, whereClause, whereArgs)
        return rowsUpdated > 0
    }

    fun addUser(user: com.example.sokoban.db.models.User): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(UserContract.UserEntry.COLUMN_USERNAME, user.username)
            put(UserContract.UserEntry.COLUMN_EMAIL, user.email)
            put(UserContract.UserEntry.COLUMN_PASSWORD_HASH, user.passwordHash)
            put(UserContract.UserEntry.COLUMN_SCORE, user.score)
            put(UserContract.UserEntry.COLUMN_LEVEL, user.level)
            put(UserContract.UserEntry.COLUMN_LAST_LOGIN, user.lastLogin)
        }

        return db.insert(UserContract.UserEntry.TABLE_NAME, null, values)
    }

/*    fun getUserScore(userId: Long): Int? {
        val db = this.readableDatabase
        var highScore: Int? = null
        val query =
            "SELECT MAX(${HighScoreContract.HighScoreEntry.COLUMN_SCORE}) AS HighScore " + //Select using MAX for highScore
                    "FROM ${HighScoreContract.HighScoreEntry.TABLE_NAME} " +
                    "WHERE ${HighScoreContract.HighScoreEntry.COLUMN_USER_ID} = ?"

        val cursor = db.rawQuery(query, arrayOf(userId.toString()))
        try {
            if (cursor.moveToFirst()) { //Check for any results
                highScore = cursor.getInt(cursor.getColumnIndexOrThrow("HighScore"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving high score: ", e)
        } finally {
            //Close cursor and db to free resources
            cursor.close()
            db.close()
        }
        return highScore //Return high score if found, else return null
    }*/

    fun getUsersByLevel(levelId: Long): List<User> {
        val db = this.readableDatabase
        val userList = mutableListOf<User>()
        val query = """
        SELECT * FROM ${UserContract.UserEntry.TABLE_NAME} 
        WHERE ${UserContract.UserEntry.COLUMN_LEVEL} = ?"""
        val cursor = db.rawQuery(query, arrayOf(levelId.toString()))
        try {
            if (cursor.moveToFirst()) {
                do {
                    val user = User(
                        id = cursor.getLong(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_ID)),
                        username = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_USERNAME)),
                        email = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_EMAIL)),
                        passwordHash = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_PASSWORD_HASH)),
                        lastLogin = cursor.getStringOrNull(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_LAST_LOGIN)),
                        score = cursor.getIntOrNull(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_SCORE)),
                        level = cursor.getLong(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_LEVEL))
                    )
                    userList.add(user)
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving users by level: ", e)
        } finally {
            cursor.close()
            db.close()
        }
        return userList
    }

    fun getLevelsByScore(minScore: Int): List<Level> {
        val db = this.readableDatabase
        val levelList = mutableListOf<Level>()
        val query = """
        SELECT * FROM ${LevelContract.LevelEntry.TABLE_NAME} 
        WHERE ${LevelContract.LevelEntry.COLUMN_REQUIRED_SCORE} > ?
    """
        val cursor = db.rawQuery(query, arrayOf(minScore.toString()))
        try {
            if (cursor.moveToFirst()) {
                do {
                    val level = Level(
                        id = cursor.getLong(cursor.getColumnIndexOrThrow(LevelContract.LevelEntry.COLUMN_ID)),
                        levelNumber = cursor.getInt(cursor.getColumnIndexOrThrow(LevelContract.LevelEntry.COLUMN_LEVEL_NUMBER)),
                        levelName = cursor.getString(cursor.getColumnIndexOrThrow(LevelContract.LevelEntry.COLUMN_LEVEL_NAME)),
                        difficulty = cursor.getInt(cursor.getColumnIndexOrThrow(LevelContract.LevelEntry.COLUMN_DIFFICULTY)),
                        requiredScore = cursor.getInt(cursor.getColumnIndexOrThrow(LevelContract.LevelEntry.COLUMN_REQUIRED_SCORE))
                    )
                    levelList.add(level)
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving levels by score: ", e)
        } finally {
            cursor.close()
            db.close()
        }
        return levelList
    }

    fun getUserById(userId: Long): User? {
        val db = this.readableDatabase
        var user: User? = null
        val query = """
        SELECT * FROM ${UserContract.UserEntry.TABLE_NAME} 
        WHERE ${UserContract.UserEntry.COLUMN_ID} = ?
    """
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))
        try {
            if (cursor.moveToFirst()) {
                user = User(
                    id = cursor.getLong(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_ID)),
                    username = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_USERNAME)),
                    email = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_EMAIL)),
                    passwordHash = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_PASSWORD_HASH)),
                    lastLogin = cursor.getStringOrNull(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_LAST_LOGIN)),
                    score = cursor.getIntOrNull(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_SCORE)),
                    level = cursor.getLong(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_LEVEL))
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving user by ID: ", e)
        } finally {
            cursor.close()
            db.close()
        }
        return user
    }

    fun getUserIdByUsernameOrEmail(usernameOrEmail: String): Long? {
        val db = this.readableDatabase
        val cursor = db.query(
            UserContract.UserEntry.TABLE_NAME,
            arrayOf(UserContract.UserEntry.COLUMN_ID),  // Make sure this is the correct column
            "${UserContract.UserEntry.COLUMN_USERNAME} = ? OR ${UserContract.UserEntry.COLUMN_EMAIL} = ?",
            arrayOf(usernameOrEmail, usernameOrEmail),
            null, null, null
        )

        return try {
            if (cursor.moveToFirst()) {
                val userId = cursor.getLong(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_ID))
                userId // Return the user ID
            } else {
                null // No user found with that username/email
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving user ID", e)
            null // Return null if there's an error
        } finally {
            cursor.close()
            db.close()
        }
    }

    fun updateUser(userId: Long, newUsername: String, newEmail: String): Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put(UserContract.UserEntry.COLUMN_USERNAME, newUsername)
        values.put(UserContract.UserEntry.COLUMN_EMAIL, newEmail)

        val rowsUpdated = db.update(
            UserContract.UserEntry.TABLE_NAME,
            values,
            "${UserContract.UserEntry.COLUMN_ID} = ?",
            arrayOf(userId.toString())
        )
        db.close()
        return rowsUpdated > 0
    }

    fun addHighScore(userId: Long, level: Int, time: Int, moves: Int, date: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(HighScoreContract.HighScoreEntry.COLUMN_USER_ID, userId)
            put(HighScoreContract.HighScoreEntry.COLUMN_LEVEL, level)
            put(HighScoreContract.HighScoreEntry.COLUMN_TIME, time)
            put(HighScoreContract.HighScoreEntry.COLUMN_MOVES, moves)
            put(HighScoreContract.HighScoreEntry.COLUMN_DATE, date)
        }

        val rowId = db.insert(HighScoreContract.HighScoreEntry.TABLE_NAME, null, values)
        db.close()
        return rowId
    }

    fun getBestHighScore(userId: Long, level: Int): Pair<Int, Int>? {
        val db = this.readableDatabase
        var bestTime: Int? = null
        var bestMoves: Int? = null
        val query = """
        SELECT ${HighScoreContract.HighScoreEntry.COLUMN_TIME}, ${HighScoreContract.HighScoreEntry.COLUMN_MOVES} 
        FROM ${HighScoreContract.HighScoreEntry.TABLE_NAME} 
        WHERE ${HighScoreContract.HighScoreEntry.COLUMN_USER_ID} = ? AND ${HighScoreContract.HighScoreEntry.COLUMN_LEVEL} = ?
        ORDER BY ${HighScoreContract.HighScoreEntry.COLUMN_TIME} ASC, ${HighScoreContract.HighScoreEntry.COLUMN_MOVES} ASC
        LIMIT 1
    """
        val cursor = db.rawQuery(query, arrayOf(userId.toString(), level.toString()))
        try {
            if (cursor.moveToFirst()) {
                bestTime = cursor.getInt(cursor.getColumnIndexOrThrow(HighScoreContract.HighScoreEntry.COLUMN_TIME))
                bestMoves = cursor.getInt(cursor.getColumnIndexOrThrow(HighScoreContract.HighScoreEntry.COLUMN_MOVES))
            }
        } finally {
            cursor.close()
            db.close()
        }
        return if (bestTime != null && bestMoves != null) Pair(bestTime, bestMoves) else null
    }

    fun getLeaderboard(): List<String> {
        val db = this.readableDatabase
        val leaderboard = mutableListOf<String>()

        val query = """
        SELECT u.${UserContract.UserEntry.COLUMN_USERNAME}, hs.${HighScoreContract.HighScoreEntry.COLUMN_LEVEL}, 
            MIN(hs.${HighScoreContract.HighScoreEntry.COLUMN_TIME}) AS best_time, 
            MIN(hs.${HighScoreContract.HighScoreEntry.COLUMN_MOVES}) AS best_moves, 
            hs.${HighScoreContract.HighScoreEntry.COLUMN_DATE}
        FROM ${HighScoreContract.HighScoreEntry.TABLE_NAME} AS hs
        JOIN ${UserContract.UserEntry.TABLE_NAME} AS u 
        ON hs.${HighScoreContract.HighScoreEntry.COLUMN_USER_ID} = u.${UserContract.UserEntry.COLUMN_ID}
        GROUP BY hs.${HighScoreContract.HighScoreEntry.COLUMN_LEVEL}, hs.${HighScoreContract.HighScoreEntry.COLUMN_USER_ID}
        ORDER BY hs.${HighScoreContract.HighScoreEntry.COLUMN_LEVEL} ASC, best_time ASC, best_moves ASC
    """

        val cursor = db.rawQuery(query, null)

        try {
            while (cursor.moveToNext()) {
                val username = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_USERNAME))
                val level = cursor.getInt(cursor.getColumnIndexOrThrow(HighScoreContract.HighScoreEntry.COLUMN_LEVEL))
                val bestTime = cursor.getInt(cursor.getColumnIndexOrThrow("best_time"))
                val bestMoves = cursor.getInt(cursor.getColumnIndexOrThrow("best_moves"))
                val date = cursor.getString(cursor.getColumnIndexOrThrow(HighScoreContract.HighScoreEntry.COLUMN_DATE))

                leaderboard.add("User: $username, Level: $level, Time: $bestTime, Moves: $bestMoves, Date: $date")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving leaderboard: ", e)
        } finally {
            cursor.close()
            db.close()
        }

        return leaderboard
    }

    fun getUserStats(userId: Long): UserStats {
        val db = this.readableDatabase
        var levelsCompleted = 0
        var totalMoves = 0
        var totalTime = 0

        val query = """
        SELECT 
            COUNT(hs.${HighScoreContract.HighScoreEntry.COLUMN_LEVEL}) AS levels_completed, 
            SUM(hs.${HighScoreContract.HighScoreEntry.COLUMN_MOVES}) AS total_moves, 
            SUM(hs.${HighScoreContract.HighScoreEntry.COLUMN_TIME}) AS total_time
        FROM ${HighScoreContract.HighScoreEntry.TABLE_NAME} AS hs
        WHERE hs.${HighScoreContract.HighScoreEntry.COLUMN_USER_ID} = ?
    """

        val cursor = db.rawQuery(query, arrayOf(userId.toString()))

        try {
            if (cursor.moveToFirst()) {
                levelsCompleted = cursor.getInt(cursor.getColumnIndexOrThrow("levels_completed"))
                totalMoves = cursor.getInt(cursor.getColumnIndexOrThrow("total_moves"))
                totalTime = cursor.getInt(cursor.getColumnIndexOrThrow("total_time"))

                // Log the results to see if the query works
                Log.d(TAG, "UserStats for userId $userId: Levels Completed = $levelsCompleted, Total Moves = $totalMoves, Total Time = $totalTime")
            } else {
                // Log if no data is found for the user
                Log.d(TAG, "No stats found for userId $userId")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving user stats: ", e)
        } finally {
            cursor.close()
            db.close()
        }

        return UserStats(levelsCompleted, totalMoves, totalTime)
    }



    // UserStats data class to hold the statistics
    data class UserStats(val levelsCompleted: Int, val totalMoves: Int, val totalTime: Int)

}

