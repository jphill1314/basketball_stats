package com.jphill.data.models

import org.jsoup.nodes.Element

class Table(element: Element) {

    val list = mutableListOf<MutableList<String>>()

    init {
        element.getElementsByTag("tr").forEach { subElement ->
            val newList = mutableListOf<String>()
            subElement.getElementsByTag("th").forEach { subsub ->
                newList.add(subsub.text())
            }
            subElement.getElementsByTag("td").forEach { subsub ->
                newList.add(subsub.text())
            }
            list.add(newList)
        }
    }

    override fun toString(): String {
        val builder = StringBuilder()
        list.forEach {
            it.forEach { text ->
                builder.append("$text ")
            }
            builder.append("\n")
        }

        return builder.toString()
    }
}