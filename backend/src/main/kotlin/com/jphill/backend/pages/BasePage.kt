package com.jphill.backend.pages

import kotlinx.html.*

fun HTML.createPage(headBlock: HEAD.() -> Unit, bodyBlock: DIV.() -> Unit) {
    head {
        link(rel = "stylesheet", href = "/static/main.css")
        headBlock()
    }
    body {
        div {
            classes = setOf("mainPage")
            div {
                classes = setOf("header")
                h1 { +"JP Basketball Stats" }
            }
            div {
                classes = setOf("topnav")
                a(href = "/") { +"Home" }
            }
            div {
                classes = setOf("content")
                bodyBlock()
            }
        }
    }
}