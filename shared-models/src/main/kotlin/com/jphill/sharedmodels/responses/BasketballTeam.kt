package com.jphill.sharedmodels.responses

data class BasketballTeam(
    val stats: BasketballTeamStats,
    val players: List<PlayerSeasonStats>
)