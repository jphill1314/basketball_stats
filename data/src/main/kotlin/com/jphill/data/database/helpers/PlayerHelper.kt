package com.jphill.data.database.helpers

import com.jphill.data.basketball.Player
import com.jphill.data.boxscore.PlayerStats
import com.jphill.data.database.tables.GameStatsTable
import com.jphill.data.database.tables.PlayerTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select

object PlayerHelper {

    fun createPlayer(id: Int): Player? {
        return PlayerTable.select { PlayerTable.id eq id }.firstOrNull()?.let {
            makePlayer(it)
        }
    }

    fun makePlayer(pRow: ResultRow): Player = Player(
        pRow[PlayerTable.id].value,
        pRow[PlayerTable.name],
        pRow[PlayerTable.position],
        GameStatsTable.select { GameStatsTable.playerId eq pRow[PlayerTable.id].value }.map { sRow ->
            createStats(pRow, sRow)
        }.toMutableList()
    )

    fun createStats(pRow: ResultRow, sRow: ResultRow): PlayerStats =
        PlayerStats(
            sRow[GameStatsTable.gameId],
            pRow[PlayerTable.name],
            pRow[PlayerTable.position],
            sRow[GameStatsTable.FGM],
            sRow[GameStatsTable.FGA],
            sRow[GameStatsTable.threeFGM],
            sRow[GameStatsTable.threeFGA],
            sRow[GameStatsTable.FTM],
            sRow[GameStatsTable.FTA],
            sRow[GameStatsTable.points],
            sRow[GameStatsTable.oRebounds],
            sRow[GameStatsTable.dRebounds],
            sRow[GameStatsTable.rebounds],
            sRow[GameStatsTable.assists],
            sRow[GameStatsTable.turnovers],
            sRow[GameStatsTable.steals],
            sRow[GameStatsTable.blocks],
            sRow[GameStatsTable.fouls]
        )
}