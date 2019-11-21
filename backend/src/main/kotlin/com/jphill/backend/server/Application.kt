package com.jphill.backend.server

import com.fasterxml.jackson.databind.SerializationFeature
import com.jphill.data.database.helpers.DatabaseHelper
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    DatabaseHelper.connectToDatabase()
    val server = embeddedServer(Netty, 8080) {
        install(ContentNegotiation) {
            jackson {
                enable(SerializationFeature.INDENT_OUTPUT)
            }
        }
        routing {
            routing()
        }
    }
    server.start(wait = true)
}