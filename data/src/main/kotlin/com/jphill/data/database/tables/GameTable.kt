package com.jphill.data.database.tables

import org.jetbrains.exposed.sql.Table

object GameTable : Table() {
    val id = integer("id").primaryKey()
    val homeTeam = varchar("home_team_name", 100) references TeamTable.name
    val awayTeam = varchar("away_team_name", 100) references TeamTable.name
    val periods = integer("periods")
}