package com.jphill.basketball.responses

import com.jphill.basketball.basketball.Team

data class BasketballTeamStats(
    val id: Int,
    val name: String,
    val eff: Double,
    val offEff: Double,
    val defEff: Double,
    val tempo: Double,
    val rawOffEff: Double,
    val rawDefEff: Double,
    val rawTempo: Double
) {

    companion object {
        fun from(team: Team) = BasketballTeamStats(
            team.id,
            team.name,
            team.getAdjEfficiency(),
            team.adjOffEff,
            team.adjDefEff,
            team.adjTempo,
            team.rawOffEff,
            team.rawDefEff,
            team.rawTempo
        )
    }
}