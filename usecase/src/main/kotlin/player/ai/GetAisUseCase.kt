package player.ai

import DomainException
import com.wsr.result.ApiResult
import com.wsr.result.map
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import player.AiRepository
import player.PlayerUseCaseModel

class GetAisUseCase(
    private val aiRepository: AiRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(): ApiResult<List<PlayerUseCaseModel>, DomainException> =
        withContext(dispatcher) {
            aiRepository.getAll().map { ais ->
                ais.map { PlayerUseCaseModel.from(it) }
            }
        }
}
