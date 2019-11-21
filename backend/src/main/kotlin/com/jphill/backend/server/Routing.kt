package com.jphill.backend.server

import com.jphill.backend.converters.createBasketballTeam
import com.jphill.backend.converters.createBasketballTeamStats
import com.jphill.backend.converters.createPlayerSeasonStats
import com.jphill.data.database.helpers.DatabaseHelper
import com.jphill.sharedmodels.responses.BasketballTeam
import com.jphill.sharedmodels.responses.BasketballTeamStats
import com.jphill.sharedmodels.responses.PlayerSeasonStats
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get

fun Routing.routing() {
    get("/") {
        call.respond(mapOf("teams" to com.jphill.backend.server.getAllTeamStats()))
    }
    get("/team") {
        val id = call.parameters["id"]?.toInt() ?: -1
        call.respond(mapOf("team" to com.jphill.backend.server.getTeam(id)))
    }
    get("/player") {
        val id = call.parameters["id"]?.toInt() ?: -1
        call.respond(mapOf("player" to com.jphill.backend.server.getPlayer(id)))
    }
}

private fun getAllTeamStats(): List<BasketballTeamStats> {
    return DatabaseHelper.createBasketballWorld().d1Teams.map { createBasketballTeamStats(it) }.sortedBy { -it.eff }
}

private fun getTeam(id: Int): BasketballTeam? {
    return DatabaseHelper.getTeam(id)?.let { createBasketballTeam(it) }
}

private fun getPlayer(id: Int): PlayerSeasonStats? {
    return DatabaseHelper.getPlayer(id)?.let { createPlayerSeasonStats(it) }
}