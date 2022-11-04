package player

import DomainException
import com.wsr.result.ApiResult

interface AiRepository {
    suspend fun getAll(): ApiResult<List<Player.Ai>, DomainException>
    suspend fun getById(id: PlayerId.AiId): ApiResult<Player.Ai, DomainException>
    suspend fun insert(ai: Player.Ai): ApiResult<Unit, DomainException>
    suspend fun update(ai: Player.Ai): ApiResult<Unit, DomainException>
}
