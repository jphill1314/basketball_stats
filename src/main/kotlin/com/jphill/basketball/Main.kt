package com.jphill.basketball

import com.jphill.basketball.basketball.Webscrapper
import com.jphill.basketball.database.*

fun main() {
//    val basketballWorld = Webscrapper.scrapeData()
    DatabaseHelper.connectToDatabase()
//    DatabaseHelper.clearDatabase()
//    DatabaseHelper.insertBasketballWorld(basketballWorld)

    val newWorld = DatabaseHelper.createBasketballWorld()
    println(newWorld.teams.size)
    println(newWorld.games.size)
    val team = newWorld.teams.values.first()
    println(team.name)
    team.players.forEach { (_, player) ->
        println(player.name)
    }
}

