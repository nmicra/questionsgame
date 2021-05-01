package com.example.game.services

import com.example.game.pojo.Answer
import com.example.game.pojo.Question
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Service
import java.net.URL


@Service
class QuestionsDataFetcher {

    private val format = Json { prettyPrint = true }

    fun fetchData() : List<Question> {
        val lines = URL("https://opentdb.com/api.php?amount=10&type=multiple").openStream().use {
            it.bufferedReader().readLines()
        }.joinToString()


        val dbQuestions = format.decodeFromString<QuestionsDbItem>(lines)
        return dbQuestions.results.map {
            val correct = Answer(it.correct_answer)
            val answers = it.incorrect_answers.map {an -> Answer(an) } + correct
            return@map Question(answers,correct.id)
        }
    }

}


@Serializable
data class QuestionsDbItem(
    val response_code: Int,
    val results: List<QuestionModel>
)

@Serializable
data class QuestionModel(
    val category: String,
    val correct_answer: String,
    val difficulty: String,
    val incorrect_answers: List<String>,
    val question: String,
    val type: String
)



