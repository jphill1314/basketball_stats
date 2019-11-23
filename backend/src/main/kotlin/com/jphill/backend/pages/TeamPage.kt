package com.jphill.backend.pages

import com.jphill.backend.converters.createBasketballTeam
import com.jphill.data.boxscore.TeamStats
import com.jphill.data.database.helpers.DatabaseHelper
import com.jphill.sharedmodels.responses.BasketballTeam
import kotlinx.html.*

object TeamPage {

    fun getTeamPageHTML(name: String): HTML.() -> Unit = {
        createPage(
            {},
            {
                getTeam(name)?.let { team ->
                    createRow {
                        createColumn { createSchedule(team) }
                        createColumn { createRoster(team) }
                    }
                }
            }
        )
    }

    fun getTeamPageJson(name: String) = mapOf("team" to getTeam(name))

    private fun DIV.createSchedule(team: BasketballTeam) {
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

    private fun DIV.createRoster(team: BasketballTeam) {
        table {
            tr {
                th { +"Name" }
                th { +"Points"}
            }
            team.players.filter { it.name != "TEAM" }.forEachIndexed { index, player ->
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