package db.seeding

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction
import player.Player
import player.PlayerId
import player.PlayerStatus
import table.PlayerModel

object PlayerDatabaseSeeder : DatabaseSeeder {
    override fun seeding(database: Database) {
        transaction(database) {
            PlayerModel.batchInsert(userData) {
                this[PlayerModel.id] = it.id.value
                this[PlayerModel.isAi] = false
            }
            PlayerModel.batchInsert(aiData) {
                this[PlayerModel.id] = it.id.value
                this[PlayerModel.isAi] = true
            }
        }
    }

    private val userData = List(5) { index ->
        Player.User.reconstruct(
            id = PlayerId.UserId("UserId$index"),
            status = PlayerStatus.WaitMatting,
        )
    }

    private val aiData = List(5) { index ->
        Player.Ai.reconstruct(
            id = PlayerId.AiId("AiId$index"),
            status = PlayerStatus.WaitMatting,
        )
    }
}
