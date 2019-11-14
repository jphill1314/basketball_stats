package com.jphill.basketball.basketball

import com.jphill.basketball.boxscore.TeamStats

class Team(
    val id: Int,
    val name: String,
    val players: MutableMap<String, Player>
) {

    fun addStatsFromGame(teamStats: TeamStats) {
        teamStats.players.forEach { player ->
            players[player.name]?.stats?.add(player) ?: run { players[player.name] = Player.from(player) }
        }
    }

    companion object {
        fun from(teamStats: TeamStats, id: Int) = Team(id, teamStats.name, mutableMapOf()).apply {
            addStatsFromGame(teamStats)
        }
    }
}