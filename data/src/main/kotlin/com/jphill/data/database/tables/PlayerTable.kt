package com.jphill.data.database.tables

import org.jetbrains.exposed.dao.IntIdTable

object PlayerTable : IntIdTable() {
    val name = varchar("name", 100)
    val position = varchar("position", 3)
    val team = integer("team_id") references TeamTable.id
}