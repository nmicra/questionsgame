package com.example.game.controller

import com.example.game.pojo.AnswerQuestionRequest
import com.example.game.pojo.AnswerQuestionResponse
import com.example.game.pojo.Question
import com.example.game.services.GameManagerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class GameManagerController {

    @Autowired
    lateinit var gameManagerService: GameManagerService

    @PostMapping("/showAvailableQuestions")
    fun showAvailableQuestions() : List<Question> {
        return gameManagerService.questions
    }

    @PostMapping("/leaderboard/{gameId}")
    fun leaderboard(@PathVariable gameId: String) : Map<String,Int>{
        return gameManagerService.getLeaderBoard(gameId)
    }

    @PostMapping("/answerQuestion")
    fun answerQuestion(@RequestBody answerQuestionRequest: AnswerQuestionRequest) : AnswerQuestionResponse {
        return gameManagerService.placeYourAnswer(answerQuestionRequest.userName, answerQuestionRequest.gameId,
                                                answerQuestionRequest.answerId,answerQuestionRequest.questionId)
    }


}