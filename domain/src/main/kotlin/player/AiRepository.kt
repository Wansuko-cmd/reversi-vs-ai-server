package player

import DomainException
import com.wsr.result.ApiResult

interface AiRepository {
    suspend fun getAll(): ApiResult<List<Player.Ai>, DomainException>
    suspend fun getById(id: PlayerId): ApiResult<Player.Ai, DomainException>
    suspend fun insert(user: Player.Ai): ApiResult<Unit, DomainException>
}
