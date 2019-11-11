package com.jphill.basketball

import com.jphill.basketball.boxscore.BoxScore
import com.jphill.basketball.boxscore.Scoreline
import com.jphill.basketball.boxscore.TeamStats
import org.jsoup.Jsoup
import java.io.File

fun main() {
    val doc = Jsoup.connect("https://stats.ncaa.org/contests/scoreboards?utf8=%E2%9C%93&sport_code=MBB&academic_year=&division=&game_date=&commit=Submit").get()
    val links = doc.getElementsByTag("a").filter { it.attr("href").contains("box_score") }
    val boxScores = mutableListOf<BoxScore>()
    links.forEach { link ->
        val boxScore = Jsoup.connect(link.attr("abs:href")).get()
        boxScores.add(BoxScore(boxScore.getElementsByTag("table")))
    }

    boxScores.forEach { boxScore ->
        val poss = boxScore.getPossessions()
        println(
            "${boxScore.homeTeam.name}: ${boxScore.homeTeam.getEfficiency(poss)}\n" +
                    "${boxScore.awayTeam.name}: ${boxScore.awayTeam.getEfficiency(poss)}\n"
        )
    }
}