package com.jphill.basketball.database.helpers

import com.jphill.basketball.basketball.Player
import com.jphill.basketball.basketball.Team
import com.jphill.basketball.database.tables.PlayerTable
import com.jphill.basketball.database.tables.TeamTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select

object TeamHelper {

    fun createTeam(id: Int): Team? {
        return TeamTable.select { TeamTable.id eq id }.firstOrNull()?.let { row -> makeTeam(row) }
    }

    fun makeTeam(row: ResultRow) = Team(
        row[TeamTable.id],
        row[TeamTable.name],
        createPlayersForTeam(row),
        row[TeamTable.isD1]
    ).apply {
        adjOffEff = row[TeamTable.adjOffEff]
        adjDefEff = row[TeamTable.adjDefEff]
        adjTempo = row[TeamTable.adjTempo]
        rawOffEff = row[TeamTable.rawOffEff]
        rawDefEff = row[TeamTable.rawDefEff]
        rawTempo = row[TeamTable.rawTempo]
    }

    private fun createPlayersForTeam(row: ResultRow): MutableMap<String, Player> {
        val map = mutableMapOf<String, Player>()
        PlayerTable.select { PlayerTable.team eq row[TeamTable.id] }.forEach { pRow ->
            val player = PlayerHelper.makePlayer(pRow)
            map[player.name] = player
        }
        return map
    }
}