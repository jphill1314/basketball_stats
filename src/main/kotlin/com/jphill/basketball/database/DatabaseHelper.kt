package com.jphill.basketball.database

import com.jphill.basketball.basketball.BasketballWorld
import com.jphill.basketball.basketball.Game
import com.jphill.basketball.basketball.Player
import com.jphill.basketball.basketball.Team
import com.jphill.basketball.boxscore.PlayerStats
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jsoup.Jsoup
import java.io.File

object DatabaseHelper {

    fun connectToDatabase() {
//        Database.connect("jdbc:h2:file:../src/main/resources/stats.db", driver = "org.h2.Driver")
        Database.connect("jdbc:h2:file:../src/main/resources/stats2019.db", driver = "org.h2.Driver")
    }

    fun clearDatabase() {
        transaction {
            SchemaUtils.create(GameTable, GameStatsTable, PlayerTable, TeamTable)
            SchemaUtils.drop(GameTable, GameStatsTable, PlayerTable, TeamTable)
        }
    }

    fun createTeamNames() {
        val file = File("C:\\Users\\jphil\\IdeaProjects\\KotlinBasketballStats\\src\\main\\resources\\2019teams.html")
        val doc = Jsoup.parse(file, "UTF-8", "www.example.com")
        val teamNames = mutableListOf<String>()
        doc.getElementsByClass("dataTable").first().getElementsByTag("tr").drop(1).dropLast(2).forEach { row ->
            println(row.getElementsByTag("td").first().text())
            teamNames.add(row.getElementsByTag("td").first().text())
        }
        transaction {
            SchemaUtils.create(D1TeamNamesTable)
            D1TeamNamesTable.batchInsert(teamNames) {
                this[D1TeamNamesTable.name] = it
            }
        }
    }

    fun getD1TeamNames(): List<String> {
        val names = mutableListOf<String>()
        transaction {
            SchemaUtils.create(D1TeamNamesTable)
            D1TeamNamesTable.selectAll().forEach { row ->
                names.add(row[D1TeamNamesTable.name])
            }
        }
        return names
    }

    fun insertBasketballWorld(basketballWorld: BasketballWorld) {
        transaction {
            SchemaUtils.create(GameTable, GameStatsTable, PlayerTable, TeamTable)

            TeamTable.batchInsert(basketballWorld.teams.toList()) { pair ->
                val team = pair.second
                this[TeamTable.id] = team.id
                this[TeamTable.name] = team.name
                this[TeamTable.isD1] = team.isD1
            }

            GameTable.batchInsert(basketballWorld.games.toList()) { pair ->
                val game = pair.second
                this[GameTable.id] = game.id
                this[GameTable.homeTeam] = game.homeTeamName
                this[GameTable.awayTeam] = game.awayTeamName
                this[GameTable.periods] = game.periods
            }

            basketballWorld.teams.forEach { (_, team) ->
                team.players.forEach { (_, player) ->
                    player.id = PlayerTable.insertAndGetId {
                        it[name] = player.name
                        it[position] = player.position
                        it[PlayerTable.team] = team.id
                    }.value
                    GameStatsTable.batchInsert(player.stats) { stats ->
                        this[GameStatsTable.playerId] = player.id
                        this[GameStatsTable.teamId] = team.id
                        this[GameStatsTable.gameId] = stats.gameId
                        this[GameStatsTable.FGM] = stats.FGM
                        this[GameStatsTable.FGA] = stats.FGA
                        this[GameStatsTable.threeFGM] = stats.threeFGM
                        this[GameStatsTable.threeFGA] = stats.threeFGA
                        this[GameStatsTable.FTM] = stats.FTM
                        this[GameStatsTable.FTA] = stats.FTA
                        this[GameStatsTable.points] = stats.points
                        this[GameStatsTable.oRebounds] = stats.oRebounds
                        this[GameStatsTable.dRebounds] = stats.dRebounds
                        this[GameStatsTable.rebounds] = stats.rebounds
                        this[GameStatsTable.assists] = stats.assists
                        this[GameStatsTable.turnovers] = stats.turnovers
                        this[GameStatsTable.steals] = stats.steals
                        this[GameStatsTable.blocks] = stats.blocks
                        this[GameStatsTable.fouls] = stats.fouls
                    }
                }
            }
        }
    }

    fun createBasketballWorld(): BasketballWorld {
        val teams = mutableMapOf<String, Team>()
        val games = mutableMapOf<Int, Game>()

        transaction {
            SchemaUtils.create(GameTable, GameStatsTable, PlayerTable, TeamTable)
            TeamTable.selectAll().forEach { row ->
                val players = mutableMapOf<String, Player>()
                PlayerTable.select { PlayerTable.team eq row[TeamTable.id] }.forEach { pRow ->
                    val stats = mutableListOf<PlayerStats>()
                    GameStatsTable.select { GameStatsTable.playerId eq pRow[PlayerTable.id].value }.forEach { sRow ->
                        stats.add(PlayerStats(
                            sRow[GameStatsTable.gameId],
                            pRow[PlayerTable.name],
                            pRow[PlayerTable.position],
                            sRow[GameStatsTable.FGM],
                            sRow[GameStatsTable.FGA],
                            sRow[GameStatsTable.threeFGM],
                            sRow[GameStatsTable.threeFGA],
                            sRow[GameStatsTable.FTM],
                            sRow[GameStatsTable.FTA],
                            sRow[GameStatsTable.points],
                            sRow[GameStatsTable.oRebounds],
                            sRow[GameStatsTable.dRebounds],
                            sRow[GameStatsTable.rebounds],
                            sRow[GameStatsTable.assists],
                            sRow[GameStatsTable.turnovers],
                            sRow[GameStatsTable.steals],
                            sRow[GameStatsTable.blocks],
                            sRow[GameStatsTable.fouls]
                        ))
                    }
                    players[pRow[PlayerTable.name]] =
                        Player(pRow[PlayerTable.id].value, pRow[PlayerTable.name], pRow[PlayerTable.position], stats)
                }
                teams[row[TeamTable.name]] = Team(row[TeamTable.id], row[TeamTable.name], players, row[TeamTable.isD1])
            }

            GameTable.selectAll().forEach { row ->
                games[row[GameTable.id]] = Game(
                    row[GameTable.id],
                    row[GameTable.homeTeam],
                    row[GameTable.awayTeam],
                    teams[row[GameTable.homeTeam]]!!,
                    teams[row[GameTable.awayTeam]]!!,
                    row[GameTable.periods]
                )
            }
        }

        return BasketballWorld(teams, games).apply { calculateStats() }
    }
}