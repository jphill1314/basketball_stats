package com.jphill.basketball.boxscore

import com.jphill.basketball.models.Table
import org.jsoup.select.Elements

class BoxScore(elements: Elements, path: String, d1TeamNames: List<String>) {

    val homeTeam: TeamStats
    val awayTeam: TeamStats
    val scoreline: Scoreline
    val id: Int

    init {
        val tables = elements.map { Table(it) }
        id = path.drop(10).dropLast(10).toInt()
        scoreline = Scoreline(tables[0])
        awayTeam = TeamStats(tables[4], id, d1TeamNames)
        homeTeam = TeamStats(tables[5], id, d1TeamNames)
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