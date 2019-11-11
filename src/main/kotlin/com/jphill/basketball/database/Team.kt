package com.jphill.basketball.database

import org.jetbrains.exposed.sql.Table

object Team : Table() {
    val id = integer("id").primaryKey()
    val name = varchar("name", 100)
}