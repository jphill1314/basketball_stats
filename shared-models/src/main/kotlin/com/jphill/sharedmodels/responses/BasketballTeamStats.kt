package com.jphill.sharedmodels.responses

data class BasketballTeamStats(
    val id: Int,
    val name: String,
    val eff: Double,
    val offEff: Double,
    val defEff: Double,
    val tempo: Double,
    val rawEff: Double,
    val rawOffEff: Double,
    val rawDefEff: Double,
    val rawTempo: Double
)