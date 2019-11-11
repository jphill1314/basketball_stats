package com.jphill.basketball.boxscore

import com.jphill.basketball.models.Table

class Scoreline(table: Table) {

    val homeTeamName: String
    val awayTeamName: String
    val homeScores: List<Int>
    val awayScores: List<Int>

    init {
        table.list[1].apply {
            awayTeamName = get(0)
            awayScores = mutableListOf(get(1).toInt(), get(2).toInt(), get(3).toInt())
        }
        table.list[2].apply {
            homeTeamName = get(0)
            homeScores = mutableListOf(get(1).toInt(), get(2).toInt(), get(3).toInt())
        }
    }


    override fun toString(): String {
        val firstLine = "$awayTeamName\t\t${awayScores[0]} ${awayScores[1]} ${awayScores[2]}"
        val secondLine = "$homeTeamName\t\t${homeScores[0]} ${homeScores[1]} ${homeScores[2]}"
        return "$firstLine\n$secondLine"
    }
}