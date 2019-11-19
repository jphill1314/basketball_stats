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

    var adjOffPasses = mutableListOf<Double>()
    var adjDefPasses = mutableListOf<Double>()
    var adjTempoPasses = mutableListOf<Double>()

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
        if (totalGames > 0) {
            rawTempo = calcRawTempo()
            adjTempoPasses.add(rawTempo)
            rawOffEff = getRawOffensiveEfficiency()
            adjOffPasses.add(rawOffEff)
            rawDefEff = getRawDefensiveEfficiency()
            adjDefPasses.add(rawDefEff)
        }
    }

    fun calculateAdjustedPass(aveTempo: Double, aveEff: Double) {
        if (totalGames > 0) {
            adjTempoPasses.add(getAdjTempo(aveTempo))
            adjOffPasses.add(getAdjOffensiveEfficiency(aveEff))
            adjDefPasses.add(getAdjDefensiveEfficiency(aveEff))
        }
    }

    fun calculateAdjustedStats(aveTempo: Double, aveEff: Double) {
        if (totalGames > 0) {
            adjTempo = getAdjTempo(aveTempo)
            adjOffEff = getAdjOffensiveEfficiency(aveEff)
            adjDefEff = getAdjDefensiveEfficiency(aveEff)
        }
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
        val passNumber = adjTempoPasses.size - 1
        for (i in possessions.indices) {
            val expected = aveTempo + (opponents[i].adjTempoPasses[passNumber] - aveTempo) + (adjTempoPasses[passNumber] - aveTempo)
            val tempo = possessions[i] * possessionAdj[i]
            val diff = (tempo / expected) - 1
            adj.add(tempo * diff + adjTempoPasses[passNumber])
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
        val passNumber = adjOffPasses.size - 1
        for (i in pointsScored.indices) {
            val expected = adjOffPasses[passNumber] + (opponents[i].adjDefPasses[passNumber] - aveEff)
            val result = pointsScored[i] / possessions[i] * 100
            val diff = (result / expected) - 1
            adj.add(expected * diff + adjOffPasses[passNumber])
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
        val passNumber = adjDefPasses.size - 1
        for (i in pointsScored.indices) {
            val expected = adjDefPasses[passNumber] + (opponents[i].adjOffPasses[passNumber] - aveEff)
            val result = pointsAllowed[i] / possessions[i] * 100
            val diff = (result / expected) - 1
            adj.add(expected * diff + adjDefPasses[passNumber])
        }
        return adj.sum() / totalGames
    }

    companion object {
        fun from(teamStats: TeamStats, id: Int) = Team(id, teamStats.name, mutableMapOf(), teamStats.isD1).apply {
            addStatsFromGame(teamStats)
        }
    }
}