package com.jphill.basketball

import com.jphill.basketball.boxscore.Webscrapper
import com.jphill.basketball.database.*

import io.ktor.application.*
import io.ktor.html.respondHtml
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.html.*

fun main() {
    val server = embeddedServer(Netty, 8080) {
        routing {
            get("/") {
                DatabaseHelper.connectToDatabase()
                val teams = DatabaseHelper.createBasketballWorld().d1Teams.sortedBy { -it.getAdjEfficiency() }
                call.respondHtml {
                    body {
                        table {
                            tr {
                                th { +"Rank" }
                                th { +"Team" }
                                th { +"Adj Eff" }
                                th { +"Adj Off Eff" }
                                th { +"Adj Def Eff" }
                                th { +"Adj Tempo" }
                                th { +"Eff" }
                                th { +"Off Eff" }
                                th { +"Def Eff" }
                                th { +"Tempo" }
                            }
                            teams.forEachIndexed { index, team ->
                                tr {
                                    td { +"${index + 1}" }
                                    td { +team.name }
                                    td { +format(team.getAdjEfficiency()) }
                                    td { +format(team.adjOffEff) }
                                    td { +format(team.adjDefEff) }
                                    td { +format(team.adjTempo) }
                                    td { +format(team.rawOffEff - team.rawDefEff) }
                                    td { +format(team.rawOffEff) }
                                    td { +format(team.rawDefEff) }
                                    td { +format(team.rawTempo) }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    server.start(wait = true)
}

private fun commandLineDBOps() {
    DatabaseHelper.connectToDatabase()
//    DatabaseHelper.createTeamNames()
//    scrapeAndSave()

    val newWorld = DatabaseHelper.createBasketballWorld()

    val stats = newWorld.d1Teams.toMutableList()
    stats.sortBy { it.getAdjEfficiency() }
    stats.forEach { println("${it.name}\t\t\t${format(it.getAdjEfficiency())} ${format(it.getRawEfficiency())}") }
//    stats.forEach { println("${it.name}\t\t\t${format(it.getAdjEfficiency())}\t${format(it.adjOffEff)}\t${format(it.adjDefEff)}") }
}

private fun format(value: Double): String {
    return String.format("%.2f", value)
}

private fun scrapeAndSave() {
    val basketballWorld = Webscrapper.scrapeData(DatabaseHelper.getD1TeamNames(), DatabaseHelper.createBasketballWorld())
    DatabaseHelper.clearDatabase()
    DatabaseHelper.insertBasketballWorld(basketballWorld)
}

