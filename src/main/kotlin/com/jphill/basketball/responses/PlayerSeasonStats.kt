package com.jphill.basketball.responses

import com.jphill.basketball.basketball.Player

data class PlayerSeasonStats(
    val id: Int,
    val name: String,
    val FGM: Int,
    val FGA: Int,
    val threeFGM: Int,
    val threeFGA: Int,
    val FTM: Int,
    val FTA: Int,
    val points: Int,
    val oRebounds: Int,
    val dRebounds: Int,
    val rebounds: Int,
    val assists: Int,
    val turnovers: Int,
    val steals: Int,
    val blocks: Int,
    val fouls: Int
) {

    companion object {
        fun from(player: Player): PlayerSeasonStats {
            player.calculateTotalStats()
            return PlayerSeasonStats(
                player.id ?: -1,
                player.name,
                player.FGM,
                player.FGA,
                player.threeFGM,
                player.threeFGA,
                player.FTM,
                player.FTA,
                player.points,
                player.oRebounds,
                player.dRebounds,
                player.rebounds,
                player.assists,
                player.turnovers,
                player.steals,
                player.blocks,
                player.fouls
            )
        }
    }
}