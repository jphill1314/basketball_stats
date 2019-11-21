package com.jphill.data.basketball

object StatsHelper {

    fun calculateRawStatsForTeam(team: Team) {
        team.apply {
            rawTempo = calcRawTempo(this)
            rawOffEff = calcRawOffensiveEfficiency(this)
            rawDefEff = calcRawDefensiveEfficiency(this)
        }
    }

    fun calculateAdjustedStatsForTeam(team: Team, aveTempo: Double, aveEff: Double) {
        team.apply {
            adjTempo = calcAdjTempo(aveTempo, team)
            adjOffEff = calcAdjOffensiveEfficiency(aveEff, team)
            adjDefEff = calcAdjDefensiveEfficiency(aveEff, team)
        }
    }

    private fun calcRawTempo(team: Team): Double {
        var total = 0.0
        for (i in team.possessions.indices) {
            total += team.possessions[i] * team.possessionAdj[i]
        }
        return total / team.totalGames
    }

    private fun calcRawOffensiveEfficiency(team: Team): Double {
        var total = 0.0
        for (i in team.pointsScored.indices) {
            total += team.pointsScored[i] / team.possessions[i]
        }
        return total / team.totalGames * 100
    }

    private fun calcRawDefensiveEfficiency(team: Team): Double {
        var total = 0.0
        for (i in team.pointsAllowed.indices) {
            total += team.pointsAllowed[i] / team.possessions[i]
        }
        return total / team.totalGames * 100
    }

    private fun calcAdjTempo(aveTempo: Double, team: Team): Double {
        val adj = mutableListOf<Double>()
        for (i in team.possessions.indices) {
            val expected = aveTempo + (team.opponents[i].rawTempo - aveTempo) + (team.rawTempo - aveTempo)
            val tempo = team.possessions[i] * team.possessionAdj[i]
            val diff = (tempo / expected) - 1
            adj.add(tempo * diff + team.rawTempo)
        }
        return adj.sum() / team.totalGames
    }

    private fun calcAdjOffensiveEfficiency(aveEff: Double, team: Team): Double {
        val adj = mutableListOf<Double>()
        for (i in team.pointsScored.indices) {
            val expected = team.rawOffEff + (team.opponents[i].rawDefEff - aveEff)
            val result = team.pointsScored[i] / team.possessions[i] * 100
            val diff = getAdjDiff((result / expected) - 1)
            adj.add(expected * diff + team.rawOffEff)
        }
        return adj.sum() / team.totalGames
    }

    private fun calcAdjDefensiveEfficiency(aveEff: Double, team: Team): Double {
        val adj = mutableListOf<Double>()
        for (i in team.pointsScored.indices) {
            val expected = team.rawDefEff + (team.opponents[i].rawOffEff - aveEff)
            val result = team.pointsAllowed[i] / team.possessions[i] * 100
            val diff = getAdjDiff((result / expected) - 1)
            adj.add(expected * diff + team.rawDefEff)
        }
        return adj.sum() / team.totalGames
    }

    private const val MAX_ADJ = 0.20
    private const val TRIGGER_ADJ = 0.10

    private fun getAdjDiff(rawDiff: Double): Double {
        return when {
            rawDiff in -TRIGGER_ADJ..TRIGGER_ADJ -> rawDiff
            rawDiff in TRIGGER_ADJ..MAX_ADJ -> TRIGGER_ADJ + (rawDiff - TRIGGER_ADJ) / 2
            rawDiff in -MAX_ADJ..-TRIGGER_ADJ -> -TRIGGER_ADJ - (rawDiff + TRIGGER_ADJ) / 2
            rawDiff > 0 -> MAX_ADJ
            else -> -MAX_ADJ
        }
    }
}