package player

import DomainException
import com.wsr.result.ApiResult

interface UserRepository {
    suspend fun getAll(): ApiResult<List<Player.User>, DomainException>
    suspend fun getById(id: PlayerId.UserId): ApiResult<Player.User, DomainException>
    suspend fun insert(user: Player.User): ApiResult<Unit, DomainException>
}
