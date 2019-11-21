package com.jphill.basketball

import com.jphill.data.boxscore.Webscrapper
import com.jphill.data.database.helpers.DatabaseHelper

fun main() {
//    DatabaseHelper.connectToDatabase()
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

