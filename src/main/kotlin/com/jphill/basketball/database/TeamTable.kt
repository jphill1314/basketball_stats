package com.jphill.basketball.database

import org.jetbrains.exposed.sql.Table

object TeamTable : Table() {
    val id = integer("id").primaryKey()
    val name = varchar("name", 100)
}