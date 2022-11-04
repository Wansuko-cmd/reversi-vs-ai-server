package room

import DomainException
import com.wsr.result.ApiResult
import com.wsr.result.flatMap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import player.PlayerId

class PlacePieceInRoomUseCase(
    private val roomRepository: RoomRepository,
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
            .flatMap { room -> roomRepository.update(room).also { println(room) } }
    }

    private fun ApiResult<Room, DomainException>.checkIsNextUser(
        playerId: PlayerId,
    ): ApiResult<Room, DomainException> = this.flatMap { room ->
        if (room.isNextUser(playerId)) ApiResult.Success(room)
        else ApiResult.Failure(DomainException.RequestValidationException("You're not next user."))
    }
}
