package player.user

import DomainException
import com.wsr.result.ApiResult
import com.wsr.result.map
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import player.PlayerUseCaseModel
import player.UserRepository

class GetUsersUseCase(
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(): ApiResult<List<PlayerUseCaseModel>, DomainException> =
        withContext(dispatcher) {
            userRepository.getAll().map { users ->
                users.map { PlayerUseCaseModel.from(it) }
            }
        }
}
