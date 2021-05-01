package com.example.game.pojo

enum class QuestionStatus {CORRECT, INCORRECT}
data class AnswerQuestionResponse(val questionStatus : QuestionStatus, val points : Int)
