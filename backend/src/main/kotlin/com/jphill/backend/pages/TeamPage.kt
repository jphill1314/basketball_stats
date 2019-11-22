package com.jphill.backend.pages

import com.jphill.backend.converters.createBasketballTeam
import com.jphill.data.boxscore.TeamStats
import com.jphill.data.database.helpers.DatabaseHelper
import com.jphill.sharedmodels.responses.BasketballTeam
import kotlinx.html.*

object TeamPage {

    fun getTeamPageHTML(name: String): HTML.() -> Unit = {
        head {
            link(rel = "stylesheet", href = "/static/main.css")
        }
        body {
            getTeam(name)?.let { team ->
                createSchedule(team)
                createRoster(team)
            }
        }
    }

    fun getTeamPageJson(name: String) = mapOf("team" to getTeam(name))

    private fun BODY.createSchedule(team: BasketballTeam) {
        table {
            tr {
                th { +"Home" }
                th { +"" }
                th { +"Away" }
            }
            team.games.forEachIndexed { index, game ->
                tr {
                    classes = setOf(if (index % 2 == 0) "even" else "odd")
                    td { a(href = "/team?id=${game.homeTeamName}") { +game.homeTeamName } }
                    td { +"${game.homeTeamScore} - ${game.awayTeamScore}" }
                    td { a(href = "/team?id=${game.awayTeamName}") {+game.awayTeamName } }
                }
            }
        }
    }

    private fun BODY.createRoster(team: BasketballTeam) {
        table {
            tr {
                th { +"Name" }
                th { +"Points"}
            }
            team.players.forEachIndexed { index, player ->
                tr {
                    classes = setOf(if (index % 2 == 0) "even" else "odd")
                    td { +player.name }
                    td { +player.points.toString() }
                }
            }
        }
    }

    private fun getTeam(name: String) : BasketballTeam? {
        return DatabaseHelper.getTeam(name)?.let { team ->
            createBasketballTeam(team, DatabaseHelper.getGamesForTeam(team))
        }
    }
}