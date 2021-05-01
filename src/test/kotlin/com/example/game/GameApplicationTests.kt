package com.example.game

import com.example.game.pojo.Answer
import com.example.game.pojo.Question
import com.example.game.services.GameManagerService
import com.example.game.services.QuestionsDataFetcher
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles


@ActiveProfiles("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // solves beforeAll "static" exception
class GameApplicationTests {

	@MockBean
	lateinit var questionsDataFetcher : QuestionsDataFetcher

	@Autowired
	lateinit var gameManagerService: GameManagerService


	@BeforeAll
	fun setupMocks() {
		val answers = (1..100).map { Answer("answer - $it") }
		val questions = (1..20).map {
			val shuffled= answers.shuffled()
			Question(shuffled.subList(0,4),shuffled[0].id) }
		Mockito.`when`(questionsDataFetcher.fetchData())
			.thenReturn(questions)
	}

	@Test
	fun testNewGameIsCreatedOnce() {
		val gameId = randID()
		assert(gameManagerService.games.none { it.id == gameId }) // there is no ID
		val q = gameManagerService.questions.random()
		gameManagerService.placeYourAnswer("kuku",gameId,q.correctAnswerId,q.id )
		assert(gameManagerService.games.any { it.id == gameId })// there is ID
		gameManagerService.placeYourAnswer("kuku",gameId,q.correctAnswerId,q.id )
		assert(gameManagerService.games.filter { it.id == gameId }.size == 1)// there is ONLY ONE ID
	}


	@Test
	fun testPointsAccumulatedPerGame() {
		val gameId1 = randID()
		val gameId2 = randID()
		(1..10).forEach {
			val q = gameManagerService.questions.random()
			assert(gameManagerService.placeYourAnswer("kuku",gameId1,q.correctAnswerId,q.id ).points == gameManagerService.pointsForCorrectAnswer.toInt() * it) }


		(1..10).forEach {
			val q = gameManagerService.questions.random()
			assert(gameManagerService.placeYourAnswer("kuku",gameId2,q.correctAnswerId,q.id ).points == gameManagerService.pointsForCorrectAnswer.toInt() * it) }


	}

	@Test
	fun testPointsNotEarnedForWrongAnswer() {
		val gameId1 = randID()
		repeat(10) {
			val q = gameManagerService.questions.random()
			val incorrectAnswer = q.availableAnswers.filterNot { answer -> answer.id == q.correctAnswerId }.first()
			assert(gameManagerService.placeYourAnswer("kuku",gameId1,incorrectAnswer.id,q.id ).points == 0) }
	}

	@Test
	fun testLeaderBoardIsCorrect() {
		val gameId1 = randID()
		repeat(10) {
			val q = gameManagerService.questions.random()
			gameManagerService.placeYourAnswer("kuku",gameId1,q.correctAnswerId,q.id ) }


		repeat(5) {
			val q = gameManagerService.questions.random()
			gameManagerService.placeYourAnswer("kuku2",gameId1,q.correctAnswerId,q.id ) }

		val leaderBoard = gameManagerService.getLeaderBoard(gameId1)
		assert(leaderBoard["kuku"] == gameManagerService.pointsForCorrectAnswer.toInt() * 10)
		assert(leaderBoard["kuku2"] == gameManagerService.pointsForCorrectAnswer.toInt() * 5)

	}

}
