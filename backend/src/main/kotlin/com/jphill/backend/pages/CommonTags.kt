package com.jphill.backend.pages

import kotlinx.html.DIV
import kotlinx.html.classes
import kotlinx.html.div

fun DIV.createRow(block: DIV.() -> Unit) {
    div {
        classes = setOf("row")
        block()
    }
}

fun DIV.createColumn(block: DIV.() -> Unit) {
    div {
        classes = setOf("column")
        block()
    }
}