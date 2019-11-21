package com.jphill.sharedmodels.responses

data class PlayerSeasonStats(
    val id: Int,
    val name: String,
    val FGM: Int,
    val FGA: Int,
    val threeFGM: Int,
    val threeFGA: Int,
    val FTM: Int,
    val FTA: Int,
    val points: Int,
    val oRebounds: Int,
    val dRebounds: Int,
    val rebounds: Int,
    val assists: Int,
    val turnovers: Int,
    val steals: Int,
    val blocks: Int,
    val fouls: Int
)