package com.example.sokoban

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.GridLayout
import android.widget.ImageView

class GameMap(
    private val gridGameMap: GridLayout,
    private val assets: AssetManager,
    private val level: Int
) {

    // Define the map for each level
    private val mapLevels = arrayOf(
        arrayOf( //Level 1
            charArrayOf('#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', 'P', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', 'B', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', 'X', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#')
        ),
        arrayOf( //Level 2
            charArrayOf('#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', '#', '#', '#', ' ', 'B', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', 'P', '#', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', '#', '#', '#', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', 'X', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#')
        ),
        arrayOf( //Level 3
            charArrayOf('#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', '#', '#', '#', ' ', '#', '#', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', '#', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', 'X', ' ', '#', '#', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', '#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', '#', ' ', 'B', '#', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', '#', ' ', 'P', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#')
        ),
        arrayOf( //Level 4
            charArrayOf('#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', '#', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', 'B', ' ', '#', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', '#', '#', '#', '#', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', '#', '#', ' ', '#', '#'),
            charArrayOf('#', ' ', ' ', ' ', 'P', ' ', ' ', '#', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', '#', 'X', ' ', ' ', '#'),
            charArrayOf('#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#')
        ),
        arrayOf( //Level 5
            charArrayOf('#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', '#', '#', '#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', '#', ' ', '#', '#', '#', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', 'X', '#', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', '#', '#', '#', '#', '#', '#', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', '#', ' ', ' ', ' ', 'B', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', '#', ' ', ' ', ' ', '#', ' ', 'P', '#'),
            charArrayOf('#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#')
        )
    )

    val themes = mapOf(
        1 to Theme(
            wallAsset = "walls/jungle.png",
            playerAsset = "characters/player.png", //<-- Switch correct image
            boxAsset = "boxes/jungleBoulder.png",
            goalAsset = "goals/jungleTarget.png",
            floorAsset = "flooring/jungleTile.png"
        ),
        2 to Theme(
            wallAsset = "walls/scifiWall.png",
            playerAsset = "characters/alien-monster.png",
            boxAsset = "boxes/jungleBoulder.png", //<-- Switch correct image
            goalAsset = "goals/scifiTarget.png",
            floorAsset = "flooring/scifiTile.png"
        ),
        3 to Theme(
            wallAsset = "walls/fantasyWall.png",
            playerAsset = "characters/alien-monster.png",
            boxAsset = "boxes/pot_of_gold.png",
            goalAsset = "goals/fantasyTarget.png",
            floorAsset = "flooring/fantasyTile.png"
        ),
        4 to Theme(
            wallAsset = "walls/ancientWall.png",
            playerAsset = "characters/alien-monster.png", //<-- Switch correct image
            boxAsset = "boxes/jungleBoulder.png", //<-- Switch correct image
            goalAsset = "goals/ancientTarget.png",
            floorAsset = "flooring/ancientTile.png"
        ),
        5 to Theme(
            wallAsset = "walls/westernWall.png",
            playerAsset = "characters/alien-monster.png", //<-- Switch correct image
            boxAsset = "boxes/jungleBoulder.png", //<-- Switch correct image
            goalAsset = "goals/westernTarget.png",
            floorAsset = "flooring/westernTile.png"
        )
    )

    private val theme: Theme = themes[level] ?: throw IllegalArgumentException("Theme not found for level $level")

    // Select the map based on the level
    var map: Array<CharArray> = mapLevels[level - 1] // Get the map based on the selected level

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

        // Dynamically calculate cell size
        val context = gridGameMap.context
        val screenWidth = context.resources.displayMetrics.widthPixels
        val cellSize = screenWidth / map[0].size // Adjust cell size based on map width

        for (y in map.indices) {
            for (x in map[y].indices) {
                val cell = map[y][x]
                val imageView = ImageView(gridGameMap.context)

                val bitmap = when (cell) {
                    '#' -> loadAssetImage(theme.wallAsset)  // Wall
                    'P' -> loadAssetImage(theme.playerAsset)  // Player
                    'B' -> loadAssetImage(theme.boxAsset)  // Box
                    'X' -> loadAssetImage(theme.goalAsset)  // Goal
                    ' ' -> loadAssetImage(theme.floorAsset) //Floor
                    else -> null  // Empty space (nothing displayed)
                }

                // Set image to transparent if bitmap is null (for empty spaces)
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap)
                } else {
                    imageView.setImageResource(android.R.color.transparent)
                }

                // Adjust image size to fit grid cell
                val params = GridLayout.LayoutParams()
                params.width = (cellSize - 5) // Set width to calculated cell size
                params.height = (cellSize -5)  // Set height to calculated cell size
                imageView.layoutParams = params

                // Add the ImageView to the grid layout
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

    data class Theme(
        val wallAsset: String,
        val playerAsset: String,
        val boxAsset: String,
        val goalAsset: String,
        val floorAsset: String
    )
}
