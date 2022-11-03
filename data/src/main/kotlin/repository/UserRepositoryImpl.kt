package repository

import DomainException
import com.wsr.result.ApiResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import player.Player
import player.PlayerId
import player.UserRepository
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
                    it[isAi] = false
                }
        }
}

fun ResultRow.toUser() = Player.User.reconstruct(
    id = PlayerId.UserId(this[PlayerModel.id]),
)
