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
import player.UserRepository

class CreateRoomUseCase(
    private val roomRepository: RoomRepository,
    private val aiRepository: AiRepository,
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(
        aiId: PlayerId.AiId,
        userId: PlayerId.UserId,
    ): ApiResult<Unit, DomainException> =
        withContext(dispatcher) {
            (aiRepository.getById(aiId) to userRepository.getById(userId))
                .let { (aiResult, userResult) ->
                    aiResult.flatMap { ai ->
                        userResult.map { user -> Room.create(ai = ai, user = user) }
                    }
                }
                .flatMap { room -> roomRepository.insert(room) }
        }
}
