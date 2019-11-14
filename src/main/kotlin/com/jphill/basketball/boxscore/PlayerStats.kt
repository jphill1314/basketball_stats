package com.jphill.basketball.boxscore

class PlayerStats(
    val gameId: Int,
    val name: String,
    val position: String,
    val FGM: Int,
    val FGA: Int,
    val threeFGM: Int,
    val threeFGA: Int,
    val FTM: Int,
    val FTA: Int,
    val points: Int,
    val oRebounds: Int,
    val dRebounds: Int,
    val rebounds: Int,
    val assists: Int,
    val turnovers: Int,
    val steals: Int,
    val blocks: Int,
    val fouls: Int
) {
    constructor(stats: Map<String, String>, gameId: Int): this(
        gameId,
        stats["Player"] ?: "",
        stats["Pos"] ?: "",
        getNumberFromString(stats["FGM"]),
        getNumberFromString(stats["FGA"]),
        getNumberFromString(stats["3FG"]),
        getNumberFromString(stats["3FGA"]),
        getNumberFromString(stats["FT"]),
        getNumberFromString(stats["FTA"]),
        getNumberFromString(stats["PTS"]),
        getNumberFromString(stats["ORebs"]),
        getNumberFromString(stats["DRebs"]),
        getNumberFromString(stats["Tot Reb"]),
        getNumberFromString(stats["AST"]),
        getNumberFromString(stats["TO"]),
        getNumberFromString(stats["STL"]),
        getNumberFromString(stats["BLK"]),
        getNumberFromString(stats["Fouls"])
    )

    override fun toString(): String {
        return "$name\t\t\t$position\t\t$FGM/$FGA\t$threeFGM/$threeFGA\t$FTM/$FTA\t" +
                "$points\t$oRebounds\t$dRebounds\t$rebounds\t$assists\t$turnovers\t$steals" +
                "\t$blocks\t$fouls"
    }

    companion object {
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
    }
}