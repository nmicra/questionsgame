package com.example.game.pojo

import com.example.game.randID

/**
 *
 * leadingBoard: key - userName, val -Score
 *
 */
data class Game(val leadingBoard : MutableMap<String,Int>,
                val id: String = randID())