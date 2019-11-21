package com.jphill.data.boxscore

import com.jphill.data.models.Table

class TeamStats(table: Table, gameId: Int, d1TeamNames: List<String>) {

    val name = table.list[0][0]
    val headings = table.list[1]
    val players = mutableListOf<PlayerStats>()
    val totalStats: PlayerStats
    val isD1: Boolean

    init {
        table.list.drop(2).forEach { player ->
            val map = mutableMapOf<String, String>()
            for (index in player.indices) {
                map[headings[index]] = player[index]
            }
            players.add(PlayerStats(map, gameId))
        }
        totalStats = players.last()
        players.remove(totalStats)
        isD1 = d1TeamNames.contains(name)
    }

    fun getPossessions(): Double {
        with(totalStats) { return FGA - oRebounds + turnovers + .475 * FTA }
    }

    fun getEfficiency(totalPos: Double): Double {
        return totalStats.points / totalPos * 100
    }

    override fun toString(): String {
        val builder = StringBuilder()
        builder.append("$name\n\n")
        players.forEach { player -> builder.append("$player\n") }
        builder.append("$totalStats\n")
        return builder.toString()
    }
}