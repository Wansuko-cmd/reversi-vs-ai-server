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
import player.PlayerStatus
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
    ): ApiResult<RoomUseCaseModel, DomainException> =
        withContext(dispatcher) {
            (aiRepository.getById(aiId) to userRepository.getById(userId))
                .let { (aiResult, userResult) ->
                    aiResult.flatMap { ai ->
                        userResult.map { user -> Room.create(ai = ai, user = user) }
                    }
                }
                .flatMap { room -> roomRepository.insert(room).map { room } }
                .updateUsers()
                .map { RoomUseCaseModel.from(it) }
        }

    private suspend fun ApiResult<Room, DomainException>.updateUsers(): ApiResult<Room, DomainException> =
        this.map { room ->
            updatePlayer(room.black.updateStatus(PlayerStatus.OnMatch(room.id)))
            updatePlayer(room.white.updateStatus(PlayerStatus.OnMatch(room.id)))
            room
        }

    private suspend fun updatePlayer(player: Player) = when (player) {
        is Player.Ai -> aiRepository.update(player)
        is Player.User -> userRepository.update(player)
    }
}
