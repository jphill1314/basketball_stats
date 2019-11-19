package com.jphill.basketball

import com.jphill.basketball.boxscore.Webscrapper
import com.jphill.basketball.database.*

fun main() {
    DatabaseHelper.connectToDatabase()
//    DatabaseHelper.createTeamNames()
//    scrapeAndSave()

    val newWorld = DatabaseHelper.createBasketballWorld()

    val stats = newWorld.d1Teams.toMutableList()
    stats.sortBy { it.adjTempo }
//    stats.forEach { println("${it.name}\t\t\t${format(it.getAdjEfficiency())}\t${format(it.adjOffEff)}\t${format(it.adjDefEff)}") }
    stats.forEach { println("${it.name}: ${it.adjTempo}") }
}

private fun format(value: Double): String {
    return String.format("%.2f", value)
}

private fun scrapeAndSave() {
    val basketballWorld = Webscrapper.scrapeData(DatabaseHelper.getD1TeamNames(), DatabaseHelper.createBasketballWorld())
    DatabaseHelper.clearDatabase()
    DatabaseHelper.insertBasketballWorld(basketballWorld)
}

