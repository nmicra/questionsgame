package com.example.game.services

import com.example.game.pojo.AnswerQuestionResponse
import com.example.game.pojo.Game
import com.example.game.pojo.Question
import com.example.game.pojo.QuestionStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class GameManagerService {

    @Value("\${points.correct.answer}")
    lateinit var pointsForCorrectAnswer : Integer

    @Autowired
    lateinit var questionsDataFetcher: QuestionsDataFetcher

    val games = mutableListOf<Game>()

    val questions : List<Question> by lazy {questionsDataFetcher.fetchData()}

    fun placeYourAnswer(userName : String, gameId : String, answerId : String,  questionId : String) : AnswerQuestionResponse {
        require(userName.isNotBlank()) { "userName should not be blank" }
        require(gameId.isNotBlank()) { "gameId should not be blank" }
        require(answerId.isNotBlank()) { "answerId should not be blank" }
        require(questionId.isNotBlank()) { "questionId should not be blank" }

        val selectedQuestion = questions.firstOrNull { it.id == questionId } ?: error("Specified questionId [$questionId] wasn't found")
        val currentGame = games.firstOrNull { it.id == gameId } ?: Game(mutableMapOf(userName to 0),gameId).also { games.add(it) }
        currentGame.leadingBoard.putIfAbsent(userName,0)
        if (selectedQuestion.correctAnswerId == answerId) {
            currentGame.leadingBoard.computeIfPresent(userName){ _, v -> v + pointsForCorrectAnswer.toInt()}
            return AnswerQuestionResponse(QuestionStatus.CORRECT, currentGame.leadingBoard[userName]!!)
        }
        return AnswerQuestionResponse(QuestionStatus.INCORRECT, currentGame.leadingBoard[userName]!!)
    }

    fun getLeaderBoard(gameId : String) : Map<String,Int>{
        require(gameId.isNotBlank()) { "gameId should not be blank" }
        return games.firstOrNull { it.id == gameId }?.leadingBoard
                                            ?.toList()?.sortedByDescending { (k,v) -> v }?.toMap() // sort by score
                                            ?: error("game [$gameId] wasn't found")
    }

}