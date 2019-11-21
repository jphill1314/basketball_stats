package com.jphill.data.basketball

import kotlin.math.round

class Game(
    val id: Int,
    val homeTeamName: String,
    val awayTeamName: String,
    val homeTeam: Team,
    val awayTeam: Team,
    val periods: Int
) {

    fun calculateStats() {
        if (!homeTeam.isD1 || !awayTeam.isD1) {
            return
        }

        val homeStats = homeTeam.calculateStats(id)
        val awayStats = awayTeam.calculateStats(id)
        val poss = round((homeStats.possessions + awayStats.possessions) / 2)

        homeTeam.apply {
            possessions.add(poss)
            possessionAdj.add(getAdjustment())
            pointsScored.add(homeStats.points)
            pointsAllowed.add(awayStats.points)
            opponents.add(awayTeam)
            totalGames++
        }
        awayTeam.apply {
            possessions.add(poss)
            possessionAdj.add(getAdjustment())
            pointsScored.add(awayStats.points)
            pointsAllowed.add(homeStats.points)
            opponents.add(homeTeam)
            totalGames++
        }
    }

    private fun getAdjustment() = GAME_LENGTH / getMinutes()

    private fun getMinutes() = GAME_LENGTH + (OT_LENGTH * (periods - 2.0))

    private companion object {
        const val GAME_LENGTH = 40
        const val OT_LENGTH = 5
    }
}