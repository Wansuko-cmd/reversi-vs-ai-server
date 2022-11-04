package player.user

import com.wsr.result.map
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import player.PlayerId
import player.PlayerUseCaseModel
import player.UserRepository

class GetUserByIdUseCase(
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(id: PlayerId.UserId) =
        withContext(dispatcher) {
            userRepository.getById(id)
                .map { user -> PlayerUseCaseModel.from(user) }
        }
}
