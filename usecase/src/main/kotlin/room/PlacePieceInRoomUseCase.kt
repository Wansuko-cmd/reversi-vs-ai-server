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

class PlacePieceInRoomUseCase(
    private val roomRepository: RoomRepository,
    private val aiRepository: AiRepository,
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(
        roomId: RoomId,
        row: Int,
        column: Int,
        playerId: PlayerId,
    ): ApiResult<Unit, DomainException> = withContext(dispatcher) {
        roomRepository
            .getById(roomId)
            .checkIsNextUser(playerId)
            .flatMap { it.place(row, column) }
            .updateUsersIfFinished()
            .flatMap { room -> roomRepository.update(room) }
    }

    private fun ApiResult<Room, DomainException>.checkIsNextUser(
        playerId: PlayerId,
    ): ApiResult<Room, DomainException> = this.flatMap { room ->
        if (room.isNextUser(playerId)) ApiResult.Success(room)
        else ApiResult.Failure(DomainException.RequestValidationException("You're not next user."))
    }

    private suspend fun ApiResult<Room, DomainException>.updateUsersIfFinished(): ApiResult<Room, DomainException> =
        this.map { room ->
            if (room.next == null) {
                updatePlayer(room.black.updateStatus(PlayerStatus.WaitMatting))
                updatePlayer(room.white.updateStatus(PlayerStatus.WaitMatting))
            }
            room
        }

    private suspend fun updatePlayer(player: Player) = when (player) {
        is Player.Ai -> aiRepository.update(player)
        is Player.User -> userRepository.update(player)
    }
}
