package com.jphill.basketball.server

import com.jphill.basketball.database.helpers.DatabaseHelper
import com.jphill.basketball.responses.BasketballTeam
import com.jphill.basketball.responses.BasketballTeamStats
import com.jphill.basketball.responses.PlayerSeasonStats
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get

fun Routing.routing() {
    get("/") {
        call.respond(mapOf("teams" to getAllTeamStats()))
    }
    get("/team") {
        val id = call.parameters["id"]?.toInt() ?: -1
        call.respond(mapOf("team" to getTeam(id)))
    }
    get("/player") {
        val id = call.parameters["id"]?.toInt() ?: -1
        call.respond(mapOf("player" to getPlayer(id)))
    }
}

private fun getAllTeamStats(): List<BasketballTeamStats> {
    return DatabaseHelper.createBasketballWorld().d1Teams.map { BasketballTeamStats.from(it) }.sortedBy { -it.eff }
}

private fun getTeam(id: Int): BasketballTeam? {
    return DatabaseHelper.getTeam(id)?.let { BasketballTeam.from(it) }
}

private fun getPlayer(id: Int): PlayerSeasonStats? {
    return DatabaseHelper.getPlayer(id)?.let { PlayerSeasonStats.from(it) }
}