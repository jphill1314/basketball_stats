package com.jphill.data.boxscore

import com.jphill.data.models.Table
import org.jsoup.select.Elements

class BoxScore(elements: Elements, path: String, d1TeamNames: List<String>) {

    val homeTeam: TeamStats
    val awayTeam: TeamStats
    val scoreline: Scoreline
    val id: Int

    init {
        val tables = elements.map { Table(it) }
        id = getIdFromPath(path)
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

    companion object {
        fun getIdFromPath(path: String) = path.drop(10).dropLast(10).toInt()
    }
}