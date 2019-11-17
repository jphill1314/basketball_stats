package com.jphill.basketball.boxscore

import com.jphill.basketball.basketball.BasketballWorld
import org.jsoup.Jsoup
import java.net.SocketTimeoutException

object Webscrapper {

    fun scrapeData(d1TeamNames: List<String>, currentWorld: BasketballWorld? = null): BasketballWorld {
        val baseUrl = "https://stats.ncaa.org"
        val basketballWorld = currentWorld ?: BasketballWorld(mutableMapOf(), mutableMapOf())

        for (date in 17..17) {
            println("Date: $date")
            val url = "https://stats.ncaa.org/season_divisions/17060/scoreboards?utf8=✓&season_division_id=&game_date=11%2F${getDate(date)}%2F2019&conference_id=0&tournament_id=&commit=Submit"
            val doc = Jsoup.connect(url).get()
            val links = doc.getElementsByTag("a").filter { it.attr("href").contains("box_score") }
            links.forEachIndexed { index, link ->
                println("Game: ${index + 1} of ${links.size}")
                val path = link.attr("href")
                if (!basketballWorld.games.containsKey(BoxScore.getIdFromPath(path))) {
                    println("New game")
                    try {
                        val boxScore = Jsoup.connect(baseUrl + path).get()
                        basketballWorld.addBoxScore(BoxScore(boxScore.getElementsByTag("table"), path, d1TeamNames))
                    } catch (e: SocketTimeoutException) {
                        println("Socket timeout for: $path")
                    }
                }
            }
        }
        basketballWorld.calculateStats()
        return basketballWorld
    }

    private fun getDate(day: Int) = "%02d".format(day)
}