package com.jphill.backend.pages

import com.jphill.backend.converters.createBasketballTeamStats
import com.jphill.data.database.helpers.DatabaseHelper
import com.jphill.sharedmodels.responses.BasketballTeamStats
import kotlinx.html.*

object HomePage {

    fun getHomePageHTML(sort: Int?): HTML.() -> Unit = {
        head {
            link(rel = "stylesheet", href = "/static/main.css")
        }
        body {
            table {
                tr {
                    th { a(href = "?sort=0") { +"Rank" } }
                    th { +"Team" }
                    th { a(href = "?sort=0") { +"Adj Eff" } }
                    th { a(href = "?sort=1") { +"Adj Off Eff" } }
                    th { a(href = "?sort=2") { +"Adj Def Eff" } }
                    th { a(href = "?sort=3") { +"Adj Tempo" } }
                    th { a(href = "?sort=4") { +"Eff" } }
                    th { a(href = "?sort=5") { +"Off Eff" } }
                    th { a(href = "?sort=6") { +"Def Eff" } }
                    th { a(href = "?sort=7") { +"Tempo" } }
                }
                getBasketballStats(sort)
                    .forEachIndexed { index, team ->
                    tr {
                        classes = setOf(if (index % 2 == 0) "even" else "odd")
                        td { +"${index + 1}" }
                        td { a(href = "/team?id=${team.name}") { +team.name } }
                        td { +format(team.eff) }
                        td { +format(team.offEff) }
                        td { +format(team.defEff) }
                        td { +format(team.tempo) }
                        td { +format(team.rawOffEff - team.rawDefEff) }
                        td { +format(team.rawOffEff) }
                        td { +format(team.rawDefEff) }
                        td { +format(team.rawTempo) }
                    }
                }
            }
        }
    }

    fun getHomePageJson(sort: Int?) = mapOf("teams" to getBasketballStats(
        sort
    )
    )

    private fun format(value: Double): String {
        return String.format("%.2f", value)
    }

    private fun getBasketballStats(sort: Int?): List<BasketballTeamStats> {
        val teams = DatabaseHelper.createBasketballWorld().d1Teams.map { createBasketballTeamStats(it) }
        return when(sort) {
            1 -> teams.sortedBy { -it.offEff }
            2 -> teams.sortedBy { it.defEff }
            3 -> teams.sortedBy { -it.tempo }
            4 -> teams.sortedBy { it.rawOffEff - it.rawDefEff }
            5 -> teams.sortedBy { -it.rawOffEff }
            6 -> teams.sortedBy { it.rawDefEff }
            7 -> teams.sortedBy { it.rawTempo }
            else -> teams.sortedBy { -it.eff }
        }
    }
}