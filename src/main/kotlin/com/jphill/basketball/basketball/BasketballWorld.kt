package com.jphill.basketball.basketball

import com.jphill.basketball.boxscore.BoxScore

class BasketballWorld(
    val teams: MutableMap<String, Team>,
    val games: MutableMap<Int, Game>
) {

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
            games[boxScore.id] = Game(boxScore.id, homeTeam.name, awayTeam.name)
        }
    }
}