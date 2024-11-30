package com.example.sokoban

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.GridLayout
import android.widget.ImageView

class GameMap(private val gridGameMap: GridLayout, private val assets: android.content.res.AssetManager) {

    var map = arrayOf(
        charArrayOf('#', '#', '#', '#', '#'),
        charArrayOf('#', 'P', ' ', ' ', '#'),
        charArrayOf('#', ' ', 'B', ' ', '#'),
        charArrayOf('#', ' ', ' ', ' ', '#'),
        charArrayOf('#', ' ', ' ', ' ', '#'),
        charArrayOf('#', ' ', ' ', 'X', '#'),
        charArrayOf('#', '#', '#', '#', '#')
    )

    private val mapWidth = map[0].size
    private val mapHeight = map.size

    val goalPositions = mutableListOf<Pair<Int, Int>>()

    init {
        findGoalPositions()
        renderMap()
    }

    fun findGoalPositions() {
        goalPositions.clear()
        for (y in map.indices) {
            for (x in map[y].indices) {
                if (map[y][x] == 'X') {
                    goalPositions.add(Pair(x, y))
                }
            }
        }
    }

    fun renderMap() {
        gridGameMap.removeAllViews() // Clear previous views
        for (y in map.indices) {
            for (x in map[y].indices) {
                val cell = map[y][x]
                val imageView = ImageView(gridGameMap.context)

                val bitmap = when (cell) {
                    '#' -> loadAssetImage("walls/jungle.png")
                    'P' -> loadAssetImage("characters/player.png")
                    'B' -> loadAssetImage("boxes/jungleBoulder.png")
                    'X' -> loadAssetImage("goals/ladybug.png")
                    else -> null
                }

                // Set image to transparent if null
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap)
                } else {
                    imageView.setImageResource(android.R.color.transparent)
                }

                val params = GridLayout.LayoutParams()
                params.width = 100
                params.height = 100
                imageView.layoutParams = params

                gridGameMap.addView(imageView)
            }
        }
    }

    private fun loadAssetImage(fileName: String): Bitmap? {
        return try {
            val inputStream = assets.open(fileName)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            null // Return null if the file is not found or any error occurs
        }
    }
}
