package com.jphill.sharedmodels.responses

class GameStats(
    val id: Int,
    val homeTeamName: String,
    val awayTeamName: String,
    val homeTeamScore: Int,
    val awayTeamScore: Int,
    val periods: Int
)