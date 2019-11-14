package com.jphill.basketball.database

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Table

object PlayerTable : IntIdTable() {
    val name = varchar("name", 100)
    val position = varchar("position", 3)
    val team = integer("team_id") references TeamTable.id
}