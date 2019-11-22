package com.jphill.data.database.helpers

import com.jphill.data.basketball.Game
import com.jphill.data.database.tables.GameTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select

object GameHelper {

    fun createGame(id: Int): Game? {
        return GameTable.select { GameTable.id eq id }.firstOrNull()?.let { makeGame(it) }
    }

    fun createGamesForTeam(name: String): List<Game> {
        val games = mutableListOf<Game>()
        GameTable.select { (GameTable.homeTeam eq name) or (GameTable.awayTeam eq name) }.forEach {
            games.add(makeGame(it))
        }
        return games
    }

    private fun makeGame(row: ResultRow): Game {
        return Game(
            row[GameTable.id],
            row[GameTable.homeTeam],
            row[GameTable.awayTeam],
            TeamHelper.createTeam(row[GameTable.homeTeam])!!,
            TeamHelper.createTeam(row[GameTable.awayTeam])!!,
            row[GameTable.periods]
        )
    }
}