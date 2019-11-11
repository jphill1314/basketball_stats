package com.jphill.basketball.boxscore

import com.jphill.basketball.models.Table
import org.jsoup.select.Elements

class BoxScore(elements: Elements) {

    val homeTeam: TeamStats
    val awayTeam: TeamStats
    val scoreline: Scoreline

    init {
        val tables = elements.map { Table(it) }
        scoreline = Scoreline(tables[0])
        awayTeam = TeamStats(tables[4])
        homeTeam = TeamStats(tables[5])
    }

    fun getPossessions(): Double {
        return (homeTeam.getPossessions() + awayTeam.getPossessions()) / 2
    }

    override fun toString(): String {
        val poss = getPossessions()
        return "$scoreline\n\n$awayTeam Off Eff: ${awayTeam.getEfficiency(poss)}" +
                "\n$homeTeam Off Eff: ${homeTeam.getEfficiency(poss)}\nPossessions: ${poss}"
    }
}