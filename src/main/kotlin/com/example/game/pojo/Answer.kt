package com.example.game.pojo

import com.example.game.randID

data class Answer(val answer : String,
                  val id : String = randID())


