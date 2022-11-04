package player.ai

import DomainException
import com.wsr.result.ApiResult
import com.wsr.result.flatMap
import com.wsr.result.map
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import player.AiRepository
import player.Player
import player.PlayerName
import player.PlayerUseCaseModel

class CreateAiUseCase(
    private val aiRepository: AiRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(name: PlayerName.AiName): ApiResult<PlayerUseCaseModel, DomainException> =
        withContext(dispatcher) {
            ApiResult.Success(Player.Ai.create(name))
                .flatMap { ai ->
                    aiRepository.insert(ai).map { PlayerUseCaseModel.from(ai) }
                }
        }
}
