package player.ai

import com.wsr.result.map
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import player.AiRepository
import player.PlayerId
import player.PlayerUseCaseModel

class GetAiByIdUseCase(
    private val aiRepository: AiRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(id: PlayerId.AiId) =
        withContext(dispatcher) {
            aiRepository.getById(id)
                .map { ai -> PlayerUseCaseModel.from(ai) }
        }
}
