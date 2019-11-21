package com.jphill.backend.converters

import com.jphill.data.basketball.Player
import com.jphill.data.basketball.Team
import com.jphill.sharedmodels.responses.BasketballTeam
import com.jphill.sharedmodels.responses.BasketballTeamStats
import com.jphill.sharedmodels.responses.PlayerSeasonStats

fun createPlayerSeasonStats(player: Player): PlayerSeasonStats {
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

fun createBasketballTeam(team: Team) = BasketballTeam(
    createBasketballTeamStats(team),
    team.players.map { (_, player) -> createPlayerSeasonStats(player) }
)

fun createBasketballTeamStats(team: Team) = BasketballTeamStats(
    team.id,
    team.name,
    team.getAdjEfficiency(),
    team.adjOffEff,
    team.adjDefEff,
    team.adjTempo,
    team.getRawEfficiency(),
    team.rawOffEff,
    team.rawDefEff,
    team.rawTempo
)