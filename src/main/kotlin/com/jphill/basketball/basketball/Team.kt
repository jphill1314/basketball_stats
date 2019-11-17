package com.jphill.basketball.basketball

import com.jphill.basketball.boxscore.TeamStats
import kotlin.math.exp

class Team(
    val id: Int,
    val name: String,
    val players: MutableMap<String, Player>,
    val isD1: Boolean
) {
    val possessions = mutableListOf<Double>()
    val possessionAdj = mutableListOf<Double>()
    val pointsScored = mutableListOf<Int>()
    val pointsAllowed = mutableListOf<Int>()
    var totalGames = 0

    val opponents = mutableListOf<Team>()

    fun addStatsFromGame(teamStats: TeamStats) {
        teamStats.players.forEach { player ->
            players[player.name]?.stats?.add(player) ?: run { players[player.name] = Player.from(player) }
        }
    }

    fun calculateStats(id: Int): StatsCalcHelper {
        var fga = 0
        var oBoards = 0
        var turnovers = 0
        var fta = 0
        var points = 0
        players.forEach { (_, player) ->
            player.getStatsForGame(id)?.let { stats ->
                fga += stats.FGA
                oBoards += stats.oRebounds
                turnovers += stats.turnovers
                fta += stats.FTA
                points += stats.points
            }
        }
        return StatsCalcHelper(
            fga - oBoards + turnovers + .475 * fta,
            points
        )
    }

    fun getRawTempo(): Double {
        var total = 0.0
        for (i in possessions.indices) {
            total += possessions[i] * possessionAdj[i]
        }
        return total / totalGames
    }

    fun getAdjTempo(aveTempo: Double): Double {
        val raw = getRawTempo()
        val adj = mutableListOf<Double>()
        for (i in possessions.indices) {
            val expected = aveTempo + (opponents[i].getRawTempo() - aveTempo) + (raw - aveTempo)
            val tempo = possessions[i] * possessionAdj[i]
            val diff = (tempo / expected) - 1
            adj.add(tempo * diff + raw)
        }
        return adj.sum() / totalGames
    }

    fun getRawOffensiveEfficiency(): Double {
        var total = 0.0
        for (i in pointsScored.indices) {
            total += pointsScored[i] / possessions[i]
        }
        return total / totalGames * 100
    }

    fun getAdjOffensiveEfficiency(aveEff: Double): Double {
        val raw = getRawOffensiveEfficiency()
        val adj = mutableListOf<Double>()
        for (i in pointsScored.indices) {
            val expected = raw + (opponents[i].getRawDefensiveEfficiency() - aveEff)
            val result = pointsScored[i] / possessions[i] * 100
            val diff = (result / expected) - 1
            adj.add(expected * diff + raw)
        }
        return adj.sum() / totalGames
    }

    fun getRawDefensiveEfficiency(): Double {
        var total = 0.0
        for (i in pointsAllowed.indices) {
            total += pointsAllowed[i] / possessions[i]
        }
        return total / totalGames * 100
    }

    fun getAdjDefensiveEfficiency(aveEff: Double): Double {
        val raw = getRawDefensiveEfficiency()
        val adj = mutableListOf<Double>()
        for (i in pointsScored.indices) {
            val expected = raw + (opponents[i].getRawOffensiveEfficiency() - aveEff)
            val result = pointsAllowed[i] / possessions[i] * 100
            val diff = (result / expected) - 1
            adj.add(expected * diff + raw)
        }
        return adj.sum() / totalGames
    }

    fun getAdjEfficiency(aveEff: Double): Double {
        return getAdjOffensiveEfficiency(aveEff) - getAdjDefensiveEfficiency(aveEff)
    }

    fun clearData() {
        possessions.clear()
        pointsScored.clear()
        pointsAllowed.clear()
        totalGames = 0

        opponents.clear()
    }

    companion object {
        fun from(teamStats: TeamStats, id: Int) = Team(id, teamStats.name, mutableMapOf(), teamStats.isD1).apply {
            addStatsFromGame(teamStats)
        }
    }
}