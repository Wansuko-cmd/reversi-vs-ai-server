package room

import DomainException
import com.wsr.result.ApiResult
import com.wsr.result.flatMap
import com.wsr.result.map
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import player.AiRepository
import player.Player
import player.PlayerId

class CreateRoomUseCase(
    private val roomRepository: RoomRepository,
    private val aiRepository: AiRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(
        aiId: PlayerId.AiId,
        userId: PlayerId.UserId,
    ): ApiResult<Unit, DomainException> =
        withContext(dispatcher) {
            aiRepository
                .getById(aiId)
                .map { ai -> Room.create(ai = ai, user = Player.User(userId)) }
                .flatMap { room -> roomRepository.insert(room) }
        }
}
