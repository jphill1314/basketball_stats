package com.jphill.basketball

import com.jphill.basketball.boxscore.Webscrapper
import com.jphill.basketball.database.*

fun main() {
    DatabaseHelper.connectToDatabase()
//    scrapeAndSave()

    val newWorld = DatabaseHelper.createBasketballWorld()
    val stats = newWorld.d1Teams.toMutableList()
    val ave = newWorld.getAverageEff()
    stats.sortBy { it.getAdjEfficiency(ave) }
    stats.forEach { println("${it.name}\t\t\t${it.getAdjEfficiency(ave)}") }
//    println("Ave Poss: ${newWorld.getAverageTempo()}")
//    println("Ave OffEff: ${newWorld.getAverageOffEff()}")
//    println("Ave DefEff: ${newWorld.getAverageDefEff()}")
//    println("Ave Eff: ${newWorld.getAverageEff()}")
}

private fun scrapeAndSave() {
    val basketballWorld = Webscrapper.scrapeData(DatabaseHelper.getD1TeamNames())
    DatabaseHelper.clearDatabase()
    DatabaseHelper.insertBasketballWorld(basketballWorld)
}

