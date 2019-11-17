package com.jphill.basketball.basketball

import com.jphill.basketball.boxscore.TeamStats

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

    var rawOffEff = 0.0
    var rawDefEff = 0.0
    var rawTempo = 0.0

    var adjOffEff = 0.0
    var adjDefEff = 0.0
    var adjTempo = 0.0

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

    fun calculateRawStats() {
        rawTempo = calcRawTempo()
        rawOffEff = getRawOffensiveEfficiency()
        rawDefEff = getRawDefensiveEfficiency()
    }

    fun calculateAdjustedStats(aveTempo: Double, aveEff: Double) {
        adjTempo = getAdjTempo(aveTempo)
        adjOffEff = getAdjOffensiveEfficiency(aveEff)
        adjDefEff = getAdjDefensiveEfficiency(aveEff)
    }

    fun getAdjEfficiency(): Double {
        return adjOffEff - adjDefEff
    }

    private fun calcRawTempo(): Double {
        var total = 0.0
        for (i in possessions.indices) {
            total += possessions[i] * possessionAdj[i]
        }
        return total / totalGames
    }

    private fun getAdjTempo(aveTempo: Double): Double {
        val adj = mutableListOf<Double>()
        for (i in possessions.indices) {
            val expected = aveTempo + (opponents[i].rawTempo - aveTempo) + (rawTempo - aveTempo)
            val tempo = possessions[i] * possessionAdj[i]
            val diff = (tempo / expected) - 1
            adj.add(tempo * diff + rawTempo)
        }
        return adj.sum() / totalGames
    }

    private fun getRawOffensiveEfficiency(): Double {
        var total = 0.0
        for (i in pointsScored.indices) {
            total += pointsScored[i] / possessions[i]
        }
        return total / totalGames * 100
    }

    private fun getAdjOffensiveEfficiency(aveEff: Double): Double {
        val adj = mutableListOf<Double>()
        for (i in pointsScored.indices) {
            val expected = rawOffEff + (opponents[i].rawDefEff - aveEff)
            val result = pointsScored[i] / possessions[i] * 100
            val diff = (result / expected) - 1
            adj.add(expected * diff + rawOffEff)
        }
        return adj.sum() / totalGames
    }

    private fun getRawDefensiveEfficiency(): Double {
        var total = 0.0
        for (i in pointsAllowed.indices) {
            total += pointsAllowed[i] / possessions[i]
        }
        return total / totalGames * 100
    }

    private fun getAdjDefensiveEfficiency(aveEff: Double): Double {
        val adj = mutableListOf<Double>()
        for (i in pointsScored.indices) {
            val expected = rawDefEff + (opponents[i].rawOffEff - aveEff)
            val result = pointsAllowed[i] / possessions[i] * 100
            val diff = (result / expected) - 1
            adj.add(expected * diff + rawDefEff)
        }
        return adj.sum() / totalGames
    }

    companion object {
        fun from(teamStats: TeamStats, id: Int) = Team(id, teamStats.name, mutableMapOf(), teamStats.isD1).apply {
            addStatsFromGame(teamStats)
        }
    }
}