package com.jphill.basketball

import com.fasterxml.jackson.databind.SerializationFeature
import com.jphill.basketball.boxscore.Webscrapper
import com.jphill.basketball.database.helpers.DatabaseHelper
import com.jphill.basketball.server.routing

import io.ktor.application.*
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    DatabaseHelper.connectToDatabase()

    val server = embeddedServer(Netty, 8080) {
        install(ContentNegotiation) {
            jackson {
                enable(SerializationFeature.INDENT_OUTPUT)
            }
        }
        routing {
            routing()
        }
    }
    server.start(wait = true)
}

private fun commandLineDBOps() {
    DatabaseHelper.connectToDatabase()
//    DatabaseHelper.createTeamNames()
    scrapeAndSave()

    val newWorld = DatabaseHelper.createBasketballWorld()

    val stats = newWorld.d1Teams.toMutableList()
    stats.sortBy { it.getAdjEfficiency() }
    stats.forEach { println("${it.name}\t\t\t${format(it.getAdjEfficiency())} ${format(it.getRawEfficiency())}") }
//    stats.forEach { println("${it.name}\t\t\t${format(it.getAdjEfficiency())}\t${format(it.adjOffEff)}\t${format(it.adjDefEff)}") }
}

private fun format(value: Double): String {
    return String.format("%.2f", value)
}

private fun scrapeAndSave() {
    val basketballWorld = Webscrapper.scrapeData(DatabaseHelper.getD1TeamNames(), DatabaseHelper.createBasketballWorld())
    DatabaseHelper.clearDatabase()
    DatabaseHelper.insertBasketballWorld(basketballWorld)
}

