package com.jphill.basketball.basketball

import com.jphill.basketball.boxscore.BoxScore

class BasketballWorld(
    val teams: MutableMap<String, Team>,
    val games: MutableMap<Int, Game>
) {
    val d1Teams: List<Team> = teams.map { (_, team) -> team }.filter { it.isD1 }

    fun addBoxScore(boxScore: BoxScore) {
        if (!games.containsKey(boxScore.id)) {
            val homeTeam = boxScore.homeTeam
            teams[homeTeam.name]?.addStatsFromGame(homeTeam) ?: run {
                teams[homeTeam.name] = Team.from(homeTeam, teams.size)
            }
            val awayTeam = boxScore.awayTeam
            teams[awayTeam.name]?.addStatsFromGame(awayTeam) ?: run {
                teams[awayTeam.name] = Team.from(awayTeam, teams.size)
            }
            games[boxScore.id] = Game(
                boxScore.id,
                homeTeam.name,
                awayTeam.name,
                teams[homeTeam.name]!!,
                teams[awayTeam.name]!!,
                boxScore.scoreline.getPeriods()
            )
        }
    }

    fun getAverageTempo() = d1Teams.map { it.rawTempo }.sum() / d1Teams.size

    fun getAverageOffEff() = d1Teams.map { it.rawOffEff }.sum() / d1Teams.size

    fun getAverageDefEff() = d1Teams.map { it.rawDefEff }.sum() / d1Teams.size

    fun getAverageEff() = (getAverageOffEff() + getAverageDefEff()) / 2

    fun calculateStats() {
        games.forEach { (_, game) -> game.calculateStats() }
        d1Teams.forEach { team -> team.calculateRawStats() }
        d1Teams.forEach { team -> team.calculateAdjustedStats(getAverageTempo(), getAverageEff()) }
    }
}