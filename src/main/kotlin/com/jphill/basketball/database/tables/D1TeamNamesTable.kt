package com.jphill.basketball.database.tables

import org.jetbrains.exposed.dao.IntIdTable

object D1TeamNamesTable : IntIdTable() {
    val name = varchar("name", 100)
}