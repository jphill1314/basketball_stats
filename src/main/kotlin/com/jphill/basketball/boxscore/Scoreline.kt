package com.jphill.basketball.boxscore

import com.jphill.basketball.models.Table

class Scoreline(table: Table) {

    val homeTeamName: String
    val awayTeamName: String
    val homeScores: List<Int>
    val awayScores: List<Int>

    init {
        // TODO: Handle overtime
        table.list[1].apply {
            awayTeamName = get(0)
            awayScores = drop(1).map { it.toInt() }
        }
        table.list[2].apply {
            homeTeamName = get(0)
            homeScores = drop(1).map { it.toInt() }
        }
    }

    fun getPeriods() = homeScores.size - 1

    override fun toString(): String {
        val firstLine = "$awayTeamName\t\t${awayScores[0]} ${awayScores[1]} ${awayScores[2]}"
        val secondLine = "$homeTeamName\t\t${homeScores[0]} ${homeScores[1]} ${homeScores[2]}"
        return "$firstLine\n$secondLine"
    }
}