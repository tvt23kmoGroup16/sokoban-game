package com.example.sokoban

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.GridLayout
import android.widget.ImageView
import com.badlogic.gdx.math.FloatCounter
import com.example.sokoban.gameItems.RandomItemGenerator
import com.example.sokoban.players.Player
import kotlin.random.Random

class GameMap(
    private val gridGameMap: GridLayout,
    private val assets: AssetManager,
    private val level: Int,
    private val stepsTaken: Int,
    val randomItemGenerator: RandomItemGenerator
) {

    // Define the map for each level
    val mapLevels = arrayOf(
        arrayOf( //Level 1
            charArrayOf('#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', 'P', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
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
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'),
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
            charArrayOf('#', ' ', ' ', '#', ' ', 'B', '#', ' ', ' ', ' ', 'O', '#'),
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
            charArrayOf('#', ' ', ' ', ' ', ' ', ' ', ' ', '#', 'X', ' ', ' ', '#'),
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
            charArrayOf('#', ' ', ' ', ' ', '#', ' ', ' ', ' ', ' ', ' ', 'P', '#'),
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
            charArrayOf('#', ' ', ' ', 'O', '#', ' ', ' ', ' ', 'B', ' ', ' ', '#'),
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
            itemAsset = mutableListOf(
                "items/speedBoots.png",
                "items/magicWand.png",
                "items/rayGun.png",
                "items/magicClub.png"
            )
        ),
        2 to Theme(
            wallAsset = "walls/scifiWall.png",
            playerAsset = "characters/alien-monster.png",
            boxAsset = "boxes/scifiBoulder.png",
            goalAsset = "goals/scifiTarget.png",
            holeAsset = "holes/scifiTile_broken.png",
            floorAsset = "flooring/scifiTile.png",
            itemAsset = mutableListOf(
                "items/speedBoots.png",
                "items/magicWand.png",
                "items/rayGun.png",
                "items/magicClub.png"
            )
        ),
        3 to Theme(
            wallAsset = "walls/fantasyWall.png",
            playerAsset = "characters/gnome.png",
            boxAsset = "boxes/pot_of_gold.png",
            goalAsset = "goals/fantasyTarget.png",
            holeAsset = "holes/fantasyTile_broken.png",
            floorAsset = "flooring/fantasyTile.png",
            itemAsset = mutableListOf(
                "items/speedBoots.png",
                "items/magicWand.png",
                "items/rayGun.png",
                "items/magicClub.png"
            )
        ),
        4 to Theme(
            wallAsset = "walls/ancientWall.png",
            playerAsset = "characters/alien-monster.png", //<-- Switch correct image
            boxAsset = "boxes/ancientBoulder.png",
            goalAsset = "goals/ancientTarget.png",
            holeAsset = "holes/ancientTile_broken.png",
            floorAsset = "flooring/ancientTile.png",
            itemAsset = mutableListOf(
                "items/speedBoots.png",
                "items/magicWand.png",
                "items/rayGun.png",
                "items/magicClub.png"
            )
        ),
        5 to Theme(
            wallAsset = "walls/westernWall.png",
            playerAsset = "characters/alien-monster.png", //<-- Switch correct image
            boxAsset = "boxes/westernBox.png",
            goalAsset = "goals/westernTarget.png",
            holeAsset = "holes/westernTile_broken.png",
            floorAsset = "flooring/westernTile.png",
            itemAsset = mutableListOf(
                "items/speedBoots.png",
                "items/magicWand.png",
                "items/rayGun.png",
                "items/magicClub.png"
            )
        ),
        6 to Theme(
            wallAsset = "walls/horrorWall.png",
            playerAsset = "characters/alien-monster.png", //<-- Switch correct image
            boxAsset = "boxes/horrorBox.png",
            goalAsset = "goals/horrorTarget.png",
            holeAsset = "holes/horrorTile_broken.png",
            floorAsset = "flooring/horrorTile.png",
            itemAsset = mutableListOf(
                "items/speedBoots.png",
                "items/magicWand.png",
                "items/rayGun.png",
                "items/magicClub.png"
            )
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
        placeRandomItems(level, stepsTaken)
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

    private val itemAsset = theme.itemAsset

    private fun getRandomItem(): String {
        return if (itemAsset.isNotEmpty()) {
            itemAsset[Random.nextInt(itemAsset.size)]
        } else
        " "
    }
    private fun placeRandomItems(level: Int, stepsTaken: Int) {
        lateinit var player: Player
               for (y in map.indices) {
            for (x in map[y].indices) {
                if (map[y][x] == ' ' && Random.nextDouble() < 0.1) { // 10% chance to place an item
                    val randomItem = randomItemGenerator.getRandomItem(player, level, stepsTaken)
                    randomItem?.let { // Only place the item if it's not null
                        map[y][x] = it.charRepresentation
                    }
                }
            }
        }
    }

    fun renderMap() {
        gridGameMap.removeAllViews() // Clear previous views

        // Dynamically calculate cell size
        val context = gridGameMap.context
        val screenWidth = context.resources.displayMetrics.widthPixels
        val cellSize = (screenWidth - gridGameMap.paddingLeft - gridGameMap.paddingRight) / map[0].size // Adjust cell size based on map width


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
                    'I' -> loadAssetImage(getRandomItem()) // Load a random item asset
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
            e.printStackTrace() //Logging error
            null // Return null if the file is not found or any error occurs
        }
    }

    data class Theme(
        val wallAsset: String,
        val playerAsset: String,
        val boxAsset: String,
        val goalAsset: String,
        val itemAsset: List<String>,
        val holeAsset: String,
        val floorAsset: String
    )
}
