package com.example.game

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.*

@SpringBootApplication
class GameApplication

fun main(args: Array<String>) {
	runApplication<GameApplication>(*args)
}

fun randID() = UUID.randomUUID().toString().replace("-","")
