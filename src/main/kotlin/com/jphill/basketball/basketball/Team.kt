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

    fun calculateStats(id: Int): StatsDataModel {
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
        return StatsDataModel(
            fga - oBoards + turnovers + .475 * fta,
            points
        )
    }

    fun getRawEfficiency(): Double {
        return rawOffEff - rawDefEff
    }

    fun getAdjEfficiency(): Double {
        return adjOffEff - adjDefEff
    }

    companion object {
        fun from(teamStats: TeamStats, id: Int) = Team(id, teamStats.name, mutableMapOf(), teamStats.isD1).apply {
            addStatsFromGame(teamStats)
        }
    }
}