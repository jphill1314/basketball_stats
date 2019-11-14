package com.jphill.basketball

import com.jphill.basketball.basketball.BasketballWorld
import com.jphill.basketball.boxscore.BoxScore
import com.jphill.basketball.database.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jsoup.Jsoup
import java.net.SocketTimeoutException

fun main() {
    val baseUrl = "https://stats.ncaa.org"
    val url = "https://stats.ncaa.org/season_divisions/17060/scoreboards?utf8=✓&season_division_id=&game_date=11%2F11%2F2019&conference_id=0&tournament_id=&commit=Submit"
    val todayUrl = "https://stats.ncaa.org/contests/scoreboards?utf8=%E2%9C%93&sport_code=MBB&academic_year=&division=&game_date=&commit=Submit"
    val doc = Jsoup.connect(url).get()
    val links = doc.getElementsByTag("a").filter { it.attr("href").contains("box_score") }
    val basketballWorld = BasketballWorld(mutableMapOf(), mutableMapOf())
    links.forEach { link ->
        val path = link.attr("href")
        try {
            val boxScore = Jsoup.connect(baseUrl + path).get()
            basketballWorld.addBoxScore(BoxScore(boxScore.getElementsByTag("table"), path))
        } catch (e: SocketTimeoutException) {
            println("Socket timeout for: $path")
        }
    }


    DatabaseHelper.connectToDatabase()
    DatabaseHelper.insertBasketballWorld(basketballWorld)

    val newWorld = DatabaseHelper.createBasketballWorld()
    newWorld.games.forEach { (_, game) ->
        println("${game.awayTeam} at ${game.homeTeam}")
    }
}