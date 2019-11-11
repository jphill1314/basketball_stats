package com.jphill.basketball.database

import org.jetbrains.exposed.sql.Table

// https://github.com/JetBrains/Exposed

object Player : Table() {
    val id = integer("id").primaryKey()
    val name = varchar("name", 100)
    val position = varchar("position", 3)
    val team = integer("team_id") references Team.id
}