package db.seeding

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import table.PlayerModel
import table.RoomModel

object Seeder : DatabaseSeeder {
    override fun seeding(database: Database) {
        transaction(database) {
            PlayerModel.insert {
                it[id] = "c4db8274-74f2-405c-a8c5-b484ba6c8a03"
                it[name] = "Hoge"
                it[isAi] = true
                it[playerStatus] = "070ed9ae-a46b-4d25-a4f8-20ee3b0cc99b"
            }
            PlayerModel.insert {
                it[id] = "9d71da6c-6429-451d-bc64-c101b9ae3264"
                it[name] = "TestUser"
                it[isAi] = false
                it[playerStatus] = "070ed9ae-a46b-4d25-a4f8-20ee3b0cc99b"
            }
        }
        transaction(database) {
            RoomModel.insert {
                it[id] = "070ed9ae-a46b-4d25-a4f8-20ee3b0cc99b"
                it[black] = "c4db8274-74f2-405c-a8c5-b484ba6c8a03"
                it[white] = "9d71da6c-6429-451d-bc64-c101b9ae3264"
                it[next] = 1
                it[board] = Board.create(8)
            }
        }
    }
}
