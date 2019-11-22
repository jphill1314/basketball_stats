package com.jphill.data.database.helpers

import com.jphill.data.basketball.BasketballWorld
import com.jphill.data.basketball.Game
import com.jphill.data.basketball.Player
import com.jphill.data.basketball.Team
import com.jphill.data.database.tables.*
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
            SchemaUtils.create(
                GameTable,
                GameStatsTable,
                PlayerTable,
                TeamTable
            )
            SchemaUtils.drop(
                GameTable,
                GameStatsTable,
                PlayerTable,
                TeamTable
            )
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
            SchemaUtils.create(
                GameTable,
                GameStatsTable,
                PlayerTable,
                TeamTable
            )

            TeamTable.batchInsert(basketballWorld.teams.toList()) { pair ->
                val team = pair.second
                this[TeamTable.id] = team.id
                this[TeamTable.name] = team.name
                this[TeamTable.isD1] = team.isD1
                this[TeamTable.adjOffEff] = team.adjOffEff
                this[TeamTable.adjDefEff] = team.adjDefEff
                this[TeamTable.adjTempo] = team.adjTempo
                this[TeamTable.rawOffEff] = team.rawOffEff
                this[TeamTable.rawDefEff] = team.rawDefEff
                this[TeamTable.rawTempo] = team.rawTempo
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

    fun updateTeamStats(basketballWorld: BasketballWorld) {
        transaction {
            SchemaUtils.create(TeamTable)
            basketballWorld.d1Teams.forEach { team ->
                TeamTable.update ({ TeamTable.id eq team.id }) {
                    it[adjOffEff] = team.adjOffEff
                    it[adjDefEff] = team.adjDefEff
                    it[adjTempo] = team.adjTempo
                    it[rawOffEff] = team.rawOffEff
                    it[rawDefEff] = team.rawDefEff
                    it[rawTempo] = team.rawTempo
                }
            }
        }
    }

    fun createBasketballWorld(): BasketballWorld {
        val teams = mutableMapOf<String, Team>()
        val games = mutableMapOf<Int, Game>()

        transaction {
            SchemaUtils.create(
                GameTable,
                GameStatsTable,
                PlayerTable,
                TeamTable
            )
            TeamTable.selectAll().forEach { row ->
                val team = TeamHelper.makeTeam(row)
                teams[team.name] = team
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

    fun getTeam(id: Int): Team? {
        var team: Team? = null
        transaction {
            SchemaUtils.create(TeamTable)
            team = TeamHelper.createTeam(id)
        }
        return team
    }

    fun getTeam(name: String): Team? {
        var team: Team? = null
        transaction {
            SchemaUtils.create(TeamTable)
            team = TeamHelper.createTeam(name)
        }
        return team
    }

    fun getPlayer(id: Int): Player? {
        var player: Player? = null
        transaction {
            SchemaUtils.create(PlayerTable)
            player = PlayerHelper.createPlayer(id)
        }
        return player
    }

    fun getGame(id: Int): Game? {
        var game: Game? = null
        transaction {
            SchemaUtils.create(GameTable)
            game = GameHelper.createGame(id)
        }
        return game
    }

    fun getGamesForTeam(team: Team): List<Game> {
        var games: List<Game> = emptyList()
        transaction {
            SchemaUtils.create(GameTable)
            games = GameHelper.createGamesForTeam(team.name)
        }
        return games
    }
}