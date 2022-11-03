package repository

import DomainException
import com.wsr.result.ApiResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import player.AiRepository
import player.Player
import player.PlayerId
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

    override suspend fun insert(user: Player.Ai): ApiResult<Unit, DomainException> =
        runCatchWithTransaction(database, dispatcher) {
            PlayerModel
                .insert {
                    it[id] = user.id.value
                    it[isAi] = true
                }
        }
}

fun ResultRow.toAi() = Player.Ai.reconstruct(
    id = PlayerId.AiId(this[PlayerModel.id]),
)
