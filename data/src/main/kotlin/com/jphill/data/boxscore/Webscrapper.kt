package com.jphill.data.boxscore

import com.jphill.data.basketball.BasketballWorld
import org.joda.time.DateTime
import org.jsoup.Jsoup
import java.net.SocketTimeoutException

object Webscrapper {

    fun scrapeData(d1TeamNames: List<String>, currentWorld: BasketballWorld? = null): BasketballWorld {
        val baseUrl = "https://stats.ncaa.org"
        val basketballWorld = currentWorld ?: BasketballWorld(mutableMapOf(), mutableMapOf())
        var currentDate = DateTime(2018, 11, 6, 12, 0)
        val endDate = DateTime(2019, 4, 8, 12, 0)

        while(currentDate <= endDate) {
            val day = currentDate.dayOfMonth().get()
            val month = currentDate.monthOfYear().get()
            val year = currentDate.year().get()
            println("Date: ${getDate(month)}/${getDate(day)}/$year")
            val doc = Jsoup.connect(getUrl(day, month, year)).get()
            val links = doc.getElementsByTag("a").filter { it.attr("href").contains("box_score") }
            links.forEachIndexed { index, link ->
                println("Game: ${index + 1} of ${links.size}")
                val path = link.attr("href")
                if (!basketballWorld.games.containsKey(BoxScore.getIdFromPath(path))) {
                    println("New game")
                    try {
                        val boxScore = Jsoup.connect(baseUrl + path).get()
                        basketballWorld.addBoxScore(
                            BoxScore(
                                boxScore.getElementsByTag("table"),
                                path,
                                d1TeamNames
                            )
                        )
                    } catch (e: SocketTimeoutException) {
                        println("Socket timeout for: $path")
                    }
                }
            }
            currentDate = currentDate.plusDays(1)
        }
        basketballWorld.calculateStats()
        return basketballWorld
    }

    private fun getUrl(day: Int, month: Int, year: Int): String {
//        return "https://stats.ncaa.org/season_divisions/17060/scoreboards?utf8=✓&season_division_id=&" +
//        "game_date=${getDate(month)}%2F${getDate(day)}%2F$year&conference_id=0&tournament_id=&commit=Submit"
        return "https://stats.ncaa.org/season_divisions/16700/scoreboards?utf8=%E2%9C%93&season_division_id=&" +
                "game_date=${getDate(month)}%2F${getDate(day)}%2F$year&conference_id=0&tournament_id=&commit=Submit"
    }

    private fun getDate(day: Int) = "%02d".format(day)
}