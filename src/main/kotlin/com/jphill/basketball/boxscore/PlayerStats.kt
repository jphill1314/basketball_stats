package com.jphill.basketball.boxscore

class PlayerStats(stats: Map<String, String>) {
    val name = stats["Player"] ?: ""
    val position = stats["Pos"] ?: ""
    val FGM = getNumberFromString(stats["FGM"])
    val FGA = getNumberFromString(stats["FGA"])
    val threeFGM = getNumberFromString(stats["3FG"])
    val threeFGA = getNumberFromString(stats["3FGA"])
    val FTM = getNumberFromString(stats["FT"])
    val FTA = getNumberFromString(stats["FTA"])
    val points = getNumberFromString(stats["PTS"])
    val oRebounds = getNumberFromString(stats["ORebs"])
    val dRebounds = getNumberFromString(stats["DRebs"])
    val rebounds = getNumberFromString(stats["Tot Reb"])
    val assists = getNumberFromString(stats["AST"])
    val turnovers = getNumberFromString(stats["TO"])
    val steals = getNumberFromString(stats["STL"])
    val blocks = getNumberFromString(stats["BLK"])
    val fouls = getNumberFromString(stats["Fouls"])

    private fun getNumberFromString(string: String?): Int {
        if (string != null && string.isNotBlank()) {
            var copy: String = string
            do {
                copy.toIntOrNull()?.let { return it }
                copy = copy.dropLast(1)
            } while (copy.isNotBlank())
        }
        return 0
    }

    override fun toString(): String {
        return "$name\t\t\t$position\t\t$FGM/$FGA\t$threeFGM/$threeFGA\t$FTM/$FTA\t" +
                "$points\t$oRebounds\t$dRebounds\t$rebounds\t$assists\t$turnovers\t$steals" +
                "\t$blocks\t$fouls"
    }
}