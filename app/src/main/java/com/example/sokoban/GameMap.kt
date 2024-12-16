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
    val mapLevels = arrayOf(
        arrayOf( //Level 1
            charArrayOf('#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', 'P', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'I', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', 'B', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', '#', '#', '#', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', 'O', ' ', ' ', ' ', ' ', ' ', '#'),
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
            charArrayOf('#', ' ', ' ', ' ', '#', '#', '#', ' ', 'O', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', 'X', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', 'O', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', 'I', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#')
        ),
        arrayOf( //Level 3
            charArrayOf('#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', 'O', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', '#', '#', '#', ' ', '#', '#', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', '#', ' ', ' ', 'O', ' ', '#'),
            charArrayOf('#', ' ', ' ', 'X', ' ', '#', '#', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', '#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', 'I', '#', ' ', 'B', '#', ' ', ' ', ' ', 'O', '#'),
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
            charArrayOf('#', ' ', ' ', ' ', ' ', 'B', ' ', '#', ' ', ' ', '0', '#'),
            charArrayOf('#', ' ', ' ', ' ', '#', '#', '#', '#', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', '#', '#', ' ', '#', '#'),
            charArrayOf('#', ' ', ' ', 'O', 'P', ' ', ' ', '#', ' ', ' ', ' ', '#'),
            charArrayOf('#', 'I', ' ', ' ', ' ', ' ', ' ', '#', 'X', ' ', ' ', '#'),
            charArrayOf('#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#')
        ),
        arrayOf( //Level 5
            charArrayOf('#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'),
            charArrayOf('#', ' ', ' ', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', '#', '#', '#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', 'O', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', '#', ' ', '#', '#', '#', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', 'X', '#', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', '#', '#', '#', '#', '#', ' ', '#', '#'),
            charArrayOf('#', ' ', ' ', 'O', '#', ' ', ' ', ' ', 'B', ' ', ' ', '#'),
            charArrayOf('#', 'I', ' ', ' ', '#', ' ', ' ', ' ', ' ', ' ', 'P', '#'),
            charArrayOf('#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#')
        ),
        arrayOf( //Level 6
            charArrayOf('#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'),
            charArrayOf('#', ' ', 'O', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', '#', '#', '#', 'O', '#', ' ', '#'),
            charArrayOf('#', 'X', '#', ' ', ' ', '#', ' ', ' ', ' ', '#', ' ', '#'),
            charArrayOf('#', '#', '#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', 'O', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', '#', ' ', '#', '#', '#', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', 'X', '#', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', '#', '#', '#', '#', '#', ' ', '#', '#'),
            charArrayOf('#', ' ', ' ', 'O', '#', ' ', 'I', ' ', 'B', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', '#', ' ', ' ', ' ', 'B', ' ', 'P', '#'),
            charArrayOf('#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#')
        )
    )

    val themes = mapOf(
        1 to Theme(
            wallAsset = "walls/jungle.png",
            playerAsset = "characters/player.png", //<-- Switch correct image
            boxAsset = "boxes/jungleBoulder.png",
            goalAsset = "goals/jungleTarget.png",
            holeAsset = "holes/jungleTile_broken.png",
            floorAsset = "flooring/jungleTile.png",
        ),
        2 to Theme(
            wallAsset = "walls/scifiWall.png",
            playerAsset = "characters/alien-monster.png",
            boxAsset = "boxes/scifiBoulder.png",
            goalAsset = "goals/scifiTarget.png",
            holeAsset = "holes/scifiTile_broken.png",
            floorAsset = "flooring/scifiTile.png",
        ),
        3 to Theme(
            wallAsset = "walls/fantasyWall.png",
            playerAsset = "characters/gnome.png",
            boxAsset = "boxes/pot_of_gold.png",
            goalAsset = "goals/fantasyTarget.png",
            holeAsset = "holes/fantasyTile_broken.png",
            floorAsset = "flooring/fantasyTile.png",
        ),
        4 to Theme(
            wallAsset = "walls/ancientWall.png",
            playerAsset = "characters/alien-monster.png", //<-- Switch correct image
            boxAsset = "boxes/ancientBoulder.png",
            goalAsset = "goals/ancientTarget.png",
            holeAsset = "holes/ancientTile_broken.png",
            floorAsset = "flooring/ancientTile.png",
        ),
        5 to Theme(
            wallAsset = "walls/westernWall.png",
            playerAsset = "characters/alien-monster.png", //<-- Switch correct image
            boxAsset = "boxes/westernBox.png",
            goalAsset = "goals/westernTarget.png",
            holeAsset = "holes/westernTile_broken.png",
            floorAsset = "flooring/westernTile.png",
            ),
        6 to Theme(
            wallAsset = "walls/horrorWall.png",
            playerAsset = "characters/alien-monster.png", //<-- Switch correct image
            boxAsset = "boxes/horrorBox.png",
            goalAsset = "goals/horrorTarget.png",
            holeAsset = "holes/horrorTile_broken.png",
            floorAsset = "flooring/horrorTile.png",
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
            val cellSize =
                (screenWidth - gridGameMap.paddingLeft - gridGameMap.paddingRight) / map[0].size // Adjust cell size based on map width

            val itemAssetMap: Map<Char, String> = mapOf(
                'S' to "assets/items/speedBoots.png",   // Speed boots
                'R' to "assets/items/rayGun.png",      // Ray gun
                'M' to "assets/items/magicClub.png",   // Magic club
                'W' to "assets/items/magicWand.png"    // Magic wand
            )

            for (y in map.indices) {
                for (x in map[y].indices) {
                    val cell = map[y][x]
                    val imageView = ImageView(gridGameMap.context)

                    val bitmap = when (cell) {
                        '#' -> loadAssetImage(theme.wallAsset)  // Wall
                        'P' -> loadAssetImage(theme.playerAsset)  // Player
                        'B' -> loadAssetImage(theme.boxAsset)  // Box
                        'X' -> loadAssetImage(theme.goalAsset)  // Goal
                        'O' -> loadAssetImage(theme.holeAsset) // Hole
                        ' ' -> loadAssetImage(theme.floorAsset) // Floor
                        'I' -> loadAssetImage(theme.floorAsset) // Floor
                        'S' -> loadAssetImage("items/speedBoots.png")
                        'M' -> loadAssetImage("items/magicClub.png")
                        'W' -> loadAssetImage("items/magicWand.png")
                        'R' -> loadAssetImage("items/raygun.png")

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
                    params.height = (cellSize - 5)  // Set height to calculated cell size
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
            e.printStackTrace() //Logging error
            null // Return null if the file is not found or any error occurs
        }
    }

    data class Theme(
        val wallAsset: String,
        val playerAsset: String,
        val boxAsset: String,
        val goalAsset: String,
        val holeAsset: String,
        val floorAsset: String
    )
}
