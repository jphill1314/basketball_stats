package com.jphill.backend.server

import com.jphill.backend.converters.createBasketballTeam
import com.jphill.backend.converters.createPlayerSeasonStats
import com.jphill.backend.pages.HomePage
import com.jphill.backend.pages.TeamPage
import com.jphill.data.database.helpers.DatabaseHelper
import com.jphill.sharedmodels.responses.BasketballTeam
import com.jphill.sharedmodels.responses.PlayerSeasonStats
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.html.respondHtml
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.util.pipeline.PipelineContext

fun Routing.routing() {
    static("/static") {
        resources("static")
    }
    get("/") {
        val sort = call.parameters["sort"]?.toInt()
        if (isJsonFormat()) {
            call.respond(HomePage.getHomePageJson(sort))
        } else {
            call.respondHtml(block = HomePage.getHomePageHTML(sort))
        }
    }
    get("/team") {
        val id = call.parameters["id"] ?: ""
        if (isJsonFormat()) {
            call.respond(TeamPage.getTeamPageJson(id))
        } else {
            call.respondHtml(block = TeamPage.getTeamPageHTML(id))
        }
    }
    get("/player") {
        val id = call.parameters["id"]?.toInt() ?: -1
        call.respond(mapOf("player" to getPlayer(id)))
    }
}

private fun PipelineContext<Unit, ApplicationCall>.isJsonFormat(): Boolean {
    return call.parameters["format"] == "json"
}

private fun getPlayer(id: Int): PlayerSeasonStats? {
    return DatabaseHelper.getPlayer(id)?.let { createPlayerSeasonStats(it) }
}