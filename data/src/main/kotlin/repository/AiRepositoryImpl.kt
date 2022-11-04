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
import player.AiRepository
import player.Player
import player.PlayerId
import player.PlayerStatus
import room.RoomId
import table.PlayerModel

class AiRepositoryImpl(
    private val database: Database,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : AiRepository {
    override suspend fun getAll(): ApiResult<List<Player.Ai>, DomainException> =
        runCatchWithTransaction(database, dispatcher) {
            PlayerModel
                .select { PlayerModel.isAi eq true }
                .map { it.toAi() }
        }

    override suspend fun getById(id: PlayerId.AiId): ApiResult<Player.Ai, DomainException> =
        runCatchWithTransaction(database, dispatcher) {
            PlayerModel
                .select { PlayerModel.isAi eq true }
                .andWhere { PlayerModel.id eq id.value }
                .first()
                .toAi()
        }

    override suspend fun insert(ai: Player.Ai): ApiResult<Unit, DomainException> =
        runCatchWithTransaction(database, dispatcher) {
            PlayerModel
                .insert {
                    it[id] = ai.id.value
                    it[isAi] = true
                    it[playerStatus] = when (ai.status) {
                        is PlayerStatus.OnMatch -> (ai.status as PlayerStatus.OnMatch).roomId.value
                        is PlayerStatus.WaitMatting -> null
                    }
                }
        }

    override suspend fun update(user: Player.Ai): ApiResult<Unit, DomainException> =
        runCatchWithTransaction(database, dispatcher) {
            PlayerModel
                .update(
                    where = { (PlayerModel.id eq user.id.value) and (PlayerModel.isAi eq true) },
                    limit = 1,
                ) {
                    it[playerStatus] = when (user.status) {
                        is PlayerStatus.OnMatch -> (user.status as PlayerStatus.OnMatch).roomId.value
                        is PlayerStatus.WaitMatting -> null
                    }
                }
        }
}

fun ResultRow.toAi() = Player.Ai.reconstruct(
    id = PlayerId.AiId(this[PlayerModel.id]),
    status = this[PlayerModel.playerStatus]
        ?.let { PlayerStatus.OnMatch(RoomId(it)) }
        ?: PlayerStatus.WaitMatting,
)
