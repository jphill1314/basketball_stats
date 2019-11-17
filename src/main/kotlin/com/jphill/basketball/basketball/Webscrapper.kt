package com.jphill.basketball.basketball

import com.jphill.basketball.boxscore.BoxScore
import org.jsoup.Jsoup
import java.net.SocketTimeoutException

object Webscrapper {

    fun scrapeData(): BasketballWorld {
        val baseUrl = "https://stats.ncaa.org"
        val basketballWorld = BasketballWorld(mutableMapOf(), mutableMapOf())

        for (date in 5..13) {
            val url = "https://stats.ncaa.org/season_divisions/17060/scoreboards?utf8=✓&season_division_id=&game_date=11%2F${getDate(date)}%2F2019&conference_id=0&tournament_id=&commit=Submit"
            val doc = Jsoup.connect(url).get()
            val links = doc.getElementsByTag("a").filter { it.attr("href").contains("box_score") }
            links.forEach { link ->
                val path = link.attr("href")
                try {
                    val boxScore = Jsoup.connect(baseUrl + path).get()
                    basketballWorld.addBoxScore(BoxScore(boxScore.getElementsByTag("table"), path))
                } catch (e: SocketTimeoutException) {
                    println("Socket timeout for: $path")
                }
            }
        }
        return basketballWorld
    }

    private fun getDate(day: Int) = "%02d".format(day)
}