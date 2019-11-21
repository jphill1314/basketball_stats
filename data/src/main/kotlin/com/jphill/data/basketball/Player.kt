package com.jphill.data.basketball

import com.jphill.data.boxscore.PlayerStats

class Player(
    var id: Int?,
    val name: String,
    val position: String,
    val stats: MutableList<PlayerStats> = mutableListOf()
) {
    var FGM = 0
    var FGA = 0
    var threeFGM = 0
    var threeFGA = 0
    var FTM = 0
    var FTA = 0
    var points = 0
    var oRebounds = 0
    var dRebounds = 0
    var rebounds = 0
    var assists = 0
    var turnovers = 0
    var steals = 0
    var blocks = 0
    var fouls = 0

    fun getStatsForGame(id: Int): PlayerStats? {
        stats.forEach { game ->
            if (game.gameId == id) {
                return game
            }
        }
        return null
    }

    fun calculateTotalStats() {
        FGM = 0
        FGA = 0
        threeFGM = 0
        threeFGA = 0
        FTM = 0
        FTA = 0
        points = 0
        oRebounds = 0
        dRebounds = 0
        rebounds = 0
        assists = 0
        turnovers = 0
        steals = 0
        blocks = 0
        fouls = 0
        stats.forEach { game ->
            FGM += game.FGM
            FGA += game.FGA
            threeFGM += game.threeFGM
            threeFGA += game.threeFGA
            FTM += game.FTM
            FTA += game.FTA
            points += game.points
            oRebounds += game.oRebounds
            dRebounds += game.dRebounds
            rebounds += game.rebounds
            assists += game.assists
            turnovers += game.turnovers
            steals += game.steals
            blocks += game.blocks
            fouls += game.fouls
        }
    }

    companion object {
        fun from(stats: PlayerStats) =
            Player(null, stats.name, stats.position, mutableListOf(stats))
    }
}