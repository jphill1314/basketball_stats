package com.jphill.basketball.database.tables

import org.jetbrains.exposed.dao.IntIdTable

object GameStatsTable : IntIdTable() {
    val playerId = (integer("player_id").references(PlayerTable.id)).nullable()
    val teamId = (integer("team_id") references TeamTable.id).nullable()
    val gameId = integer("game_id") references GameTable.id
    val FGM = integer("FGM")
    val FGA = integer("FGA")
    val threeFGM = integer("threeFGM")
    val threeFGA = integer("threeFGA")
    val FTM = integer("FTM")
    val FTA = integer("FTA")
    val points = integer("points")
    val oRebounds = integer("oRebounds")
    val dRebounds = integer("dRebounds")
    val rebounds = integer("rebounds")
    val assists = integer("assists")
    val turnovers = integer("turnovers")
    val steals = integer("steals")
    val blocks = integer("blocks")
    val fouls = integer("fouls")
}