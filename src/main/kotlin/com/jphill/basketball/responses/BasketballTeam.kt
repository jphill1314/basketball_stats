package com.jphill.basketball.responses

import com.jphill.basketball.basketball.Team

data class BasketballTeam(
    val stats: BasketballTeamStats,
    val players: List<PlayerSeasonStats>
) {

    companion object {
        fun from(team: Team) = BasketballTeam(
            BasketballTeamStats.from(team),
            team.players.map { (_, player) -> PlayerSeasonStats.from(player) }
        )
    }
}