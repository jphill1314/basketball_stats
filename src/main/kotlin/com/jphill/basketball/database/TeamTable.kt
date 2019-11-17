package com.jphill.basketball.database

import org.jetbrains.exposed.sql.Table

object TeamTable : Table() {
    val id = integer("id").primaryKey()
    val name = varchar("name", 100)
    val isD1 = bool("is_d1")
}