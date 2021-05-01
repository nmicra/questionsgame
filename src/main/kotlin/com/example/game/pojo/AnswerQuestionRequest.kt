package com.example.game.pojo

data class AnswerQuestionRequest(val userName : String,
                                 val gameId : String,
                                 val answerId : String,
                                 val questionId : String
)
