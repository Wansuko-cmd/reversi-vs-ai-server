package repository

import DomainException
import com.wsr.result.ApiResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import player.Player
import player.PlayerId
import player.PlayerName
import player.PlayerStatus
import player.UserRepository
import room.RoomId
import table.PlayerModel

class UserRepositoryImpl(
    private val database: Database,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : UserRepository {
    override suspend fun getAll(): ApiResult<List<Player.User>, DomainException> =
        runCatchWithTransaction(database, dispatcher) {
            PlayerModel
                .select { PlayerModel.isAi eq false }
                .map { it.toUser() }
        }

    override suspend fun getById(id: PlayerId.UserId): ApiResult<Player.User, DomainException> =
        runCatchWithTransaction(database, dispatcher) {
            PlayerModel
                .select { PlayerModel.isAi eq false }
                .andWhere { PlayerModel.id eq id.value }
                .first()
                .toUser()
        }

    override suspend fun insert(user: Player.User): ApiResult<Unit, DomainException> =
        runCatchWithTransaction(database, dispatcher) {
            PlayerModel
                .insert {
                    it[id] = user.id.value
                    it[name] = user.name.value
                    it[isAi] = false
                    it[playerStatus] = when (user.status) {
                        is PlayerStatus.OnMatch -> (user.status as PlayerStatus.OnMatch).roomId.value
                        is PlayerStatus.WaitMatting -> null
                    }
                }
        }

    override suspend fun update(user: Player.User): ApiResult<Unit, DomainException> =
        runCatchWithTransaction(database, dispatcher) {
            PlayerModel
                .update(
                    where = { (PlayerModel.id eq user.id.value) and (PlayerModel.isAi eq false) },
                    limit = 1,
                ) {
                    it[name] = user.name.value
                    it[playerStatus] = when (user.status) {
                        is PlayerStatus.OnMatch -> (user.status as PlayerStatus.OnMatch).roomId.value
                        is PlayerStatus.WaitMatting -> null
                    }
                }
        }
}

fun ResultRow.toUser() = Player.User.reconstruct(
    id = PlayerId.UserId(this[PlayerModel.id]),
    name = PlayerName.UserName(this[PlayerModel.name]),
    status = this[PlayerModel.playerStatus]
        ?.let { PlayerStatus.OnMatch(RoomId(it)) }
        ?: PlayerStatus.WaitMatting,
)
