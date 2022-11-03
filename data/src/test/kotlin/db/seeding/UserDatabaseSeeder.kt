package db.seeding

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction
import table.PlayerModel
import user.User
import user.UserId
import user.UserName

object UserDatabaseSeeder : DatabaseSeeder {
    override fun seeding(database: Database) {
        transaction(database) {
            PlayerModel.batchInsert(userData) {
                this[PlayerModel.id] = it.id.value
                this[PlayerModel.name] = it.name.value
            }
        }
    }

    private val userData = List(10) { index ->
        User.reconstruct(
            id = UserId("UserId$index"),
            name = UserName("UserName$index"),
        )
    }
}
