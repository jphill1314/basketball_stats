package com.jphill.basketball.database

import org.jetbrains.exposed.sql.Table

object GameStats : Table() {
    val id = integer("id").primaryKey()
    val playerId = integer("player_id") references Player.id
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