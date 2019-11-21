package com.jphill.data.database.tables

import org.jetbrains.exposed.sql.Table

object TeamTable : Table() {
    val id = integer("id").primaryKey()
    val name = varchar("name", 100)
    val isD1 = bool("is_d1")
    val adjOffEff = double("adj_off_eff")
    val adjDefEff = double("adj_def_eff")
    val adjTempo = double("adj_tempo")
    val rawOffEff = double("raw_off_eff")
    val rawDefEff = double("raw_def_eff")
    val rawTempo = double("raw_tempo")
}