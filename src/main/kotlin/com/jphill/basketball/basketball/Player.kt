package com.jphill.basketball.basketball

import com.jphill.basketball.boxscore.PlayerStats

class Player(
    var id: Int?,
    val name: String,
    val position: String,
    val stats: MutableList<PlayerStats> = mutableListOf()
) {

    companion object {
        fun from(stats: PlayerStats) = Player(null, stats.name, stats.position, mutableListOf(stats))
    }
}