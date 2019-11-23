package com.jphill.backend.converters

import com.jphill.data.basketball.Game
import com.jphill.data.basketball.Player
import com.jphill.data.basketball.Team
import com.jphill.sharedmodels.responses.BasketballTeam
import com.jphill.sharedmodels.responses.BasketballTeamStats
import com.jphill.sharedmodels.responses.GameStats
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

fun createBasketballTeam(team: Team, games: List<Game>) = BasketballTeam(
    createBasketballTeamStats(team),
    team.players.map { (_, player) -> createPlayerSeasonStats(player) },
    games.map { createGameStats(it) }
)

fun createBasketballTeamStats(team: Team) = BasketballTeamStats(
    team.id,
    getCorrectName(team.name),
    team.getAdjEfficiency(),
    team.adjOffEff,
    team.adjDefEff,
    team.adjTempo,
    team.getRawEfficiency(),
    team.rawOffEff,
    team.rawDefEff,
    team.rawTempo
)

private fun getCorrectName(name: String): String {
    return if (name.contains("<")) {
        name.substringBefore("<")
    } else {
        name
    }
}

fun createGameStats(game: Game) = GameStats(
    game.id,
    getCorrectName(game.homeTeamName),
    getCorrectName(game.awayTeamName),
    game.homeTeam.calculateStats(game.id).points,
    game.awayTeam.calculateStats(game.id).points,
    game.periods
)