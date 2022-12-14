package player.user

import DomainException
import com.wsr.result.ApiResult
import com.wsr.result.flatMap
import com.wsr.result.map
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import player.Player
import player.PlayerName
import player.PlayerUseCaseModel
import player.UserRepository

class CreateUserUseCase(
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(name: PlayerName.UserName): ApiResult<PlayerUseCaseModel, DomainException> =
        withContext(dispatcher) {
            ApiResult.Success(Player.User.create(name))
                .flatMap { user ->
                    userRepository.insert(user).map { PlayerUseCaseModel.from(user) }
                }
        }
}
