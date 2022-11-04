package db.seeding

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction
import player.Player
import player.PlayerId
import player.PlayerStatus
import room.Room
import room.RoomId
import table.RoomModel

object RoomDatabaseSeeder : DatabaseSeeder {
    override fun seeding(database: Database) {
        transaction(database) {
            RoomModel.batchInsert(roomData) {
                this[RoomModel.id] = it.id.value
                this[RoomModel.black] = it.black.id.value
                this[RoomModel.white] = it.white.id.value
                this[RoomModel.next] = it.next?.toInt()
                this[RoomModel.board] = it.board
            }
        }
    }

    private val roomData = List(5) { index ->
        Room.reconstruct(
            id = RoomId("RoomId$index"),
            black = Player.User.reconstruct(
                id = PlayerId.UserId("UserId$index"),
                status = PlayerStatus.WaitMatting,
            ),
            white = Player.Ai.reconstruct(
                id = PlayerId.AiId("UserId$index"),
                status = PlayerStatus.WaitMatting,
            ),
            next = Cell.Piece.Black,
            board = Board.create(8),
        )
    }
}
