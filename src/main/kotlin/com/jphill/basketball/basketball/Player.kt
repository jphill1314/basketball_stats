package com.jphill.basketball.basketball

import com.jphill.basketball.boxscore.PlayerStats

class Player(
    var id: Int?,
    val name: String,
    val position: String,
    val stats: MutableList<PlayerStats> = mutableListOf()
) {

    fun getStatsForGame(id: Int): PlayerStats? {
        stats.forEach { game ->
            if (game.gameId == id) {
                return game
            }
        }
        return null
    }

    companion object {
        fun from(stats: PlayerStats) = Player(null, stats.name, stats.position, mutableListOf(stats))
    }
}