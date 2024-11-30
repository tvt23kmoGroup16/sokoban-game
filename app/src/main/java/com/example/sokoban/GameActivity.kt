package com.example.sokoban

import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {

    private lateinit var gridGameMap: GridLayout
    private lateinit var btnUp: Button
    private lateinit var btnDown: Button
    private lateinit var btnLeft: Button
    private lateinit var btnRight: Button

    private lateinit var gameMap: GameMap
    private lateinit var playerMovement: PlayerMovement

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        gridGameMap = findViewById(R.id.grid_game_map)
        btnUp = findViewById(R.id.btn_up)
        btnDown = findViewById(R.id.btn_down)
        btnLeft = findViewById(R.id.btn_left)
        btnRight = findViewById(R.id.btn_right)

        gameMap = GameMap(gridGameMap, assets)
        playerMovement = PlayerMovement(this, gameMap)

        btnUp.setOnClickListener { playerMovement.movePlayer(0, -1) }
        btnDown.setOnClickListener { playerMovement.movePlayer(0, 1) }
        btnLeft.setOnClickListener { playerMovement.movePlayer(-1, 0) }
        btnRight.setOnClickListener { playerMovement.movePlayer(1, 0) }
    }
}
