package com.example.sokoban

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.sokoban.db.DatabaseHelper

class LeaderboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        // Get a reference to the TableLayout
        val tableLayout = findViewById<TableLayout>(R.id.table_leaderboard)

        // Set divider and show dividers between rows and columns
        tableLayout.setShowDividers(TableLayout.SHOW_DIVIDER_MIDDLE)
        tableLayout.dividerDrawable = resources.getDrawable(R.drawable.table_divider)

        // Get leaderboard data from DatabaseHelper
        val dbHelper = DatabaseHelper(this)
        val leaderboardData = dbHelper.getLeaderboard()

        // Create the header row for the TableLayout (only once)
        createHeaderRow(tableLayout)

        // Iterate through the leaderboard data and create rows
        leaderboardData.forEach { data ->
            createDataRow(tableLayout, data)
        }
    }

    // Function to create the header row for the leaderboard table
    private fun createHeaderRow(tableLayout: TableLayout) {
        val headerRow = TableRow(this)

        // Define layout params for header row with weights
        val params = TableRow.LayoutParams(
            0, // This makes the width adjust based on weight
            TableRow.LayoutParams.WRAP_CONTENT,
            1f
        )
        headerRow.layoutParams = params

        // Define headers
        val headers = listOf("User", "Level", "Time", "Moves", "Date")
        headers.forEachIndexed { index, header ->
            val textView = TextView(this)
            textView.text = header
            textView.setTypeface(null, Typeface.BOLD) // Bold text for header
            textView.layoutParams = TableRow.LayoutParams(
                0, // This makes the width adjust based on weight
                TableRow.LayoutParams.WRAP_CONTENT,
                when (index) {
                    0 -> 1.5f // User
                    1, 2, 3 -> 0.8f // Level, Time, Moves (smaller space)
                    4 -> 2f // Date (most space)
                    else -> 1f
                }
            )
            textView.setPadding(8, 8, 8, 8)
            textView.setTextColor(Color.BLACK)
            textView.setBackgroundColor(Color.LTGRAY)
            headerRow.addView(textView)
        }

        tableLayout.addView(headerRow)
    }

    private fun createDataRow(tableLayout: TableLayout, data: String) {
        val dataRow = TableRow(this)

        // LayoutParams for data row (same structure as the header)
        val params = TableRow.LayoutParams(
            0, // This makes the width adjust based on weight
            TableRow.LayoutParams.WRAP_CONTENT,
            1f
        )
        dataRow.layoutParams = params

        val components = data.split(", ")
        components.forEachIndexed { index, it ->
            val value = it.split(": ")[1] // Extract only the value

            val columnWeight = when (index) {
                0 -> 1.5f // User column
                1, 2, 3 -> 0.8f // Level, Time, Moves (smaller space)
                4 -> 2f // Date column (more space)
                else -> 1f
            }

            val textView = TextView(this)
            textView.text = value
            val params = TableRow.LayoutParams(
                0, // This makes the width adjust based on weight
                TableRow.LayoutParams.WRAP_CONTENT,
                columnWeight
            )
            textView.layoutParams = params
            textView.setPadding(8, 8, 8, 8)
            textView.setTextColor(Color.BLACK)
            textView.setBackgroundColor(Color.WHITE)
            dataRow.addView(textView)
        }

        tableLayout.addView(dataRow)
    }

}
