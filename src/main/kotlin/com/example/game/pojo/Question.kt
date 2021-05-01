package com.example.game.pojo

import com.example.game.randID

data class Question (val availableAnswers : List<Answer>,
                     val correctAnswerId : String,
                     val id : String = randID())