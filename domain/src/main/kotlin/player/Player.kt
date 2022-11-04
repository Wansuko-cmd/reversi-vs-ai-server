package player

import room.RoomId
import java.util.UUID

sealed interface Player {
    val id: PlayerId
    val status: PlayerStatus
    fun updateStatus(status: PlayerStatus): Player = when (this) {
        is Ai -> Player.Ai.reconstruct(
            id = id,
            status = status,
        )
        is User -> User.reconstruct(
            id = id,
            status = status,
        )
    }
    class Ai private constructor(
        override val id: PlayerId.AiId,
        override val status: PlayerStatus,
    ) : Player {

        companion object {
            fun create() = Ai(
                id = PlayerId.AiId(UUID.randomUUID().toString()),
                status = PlayerStatus.WaitMatting,
            )
            fun reconstruct(
                id: PlayerId.AiId,
                status: PlayerStatus,
            ) = Ai(id = id, status = status)
        }
    }
    class User private constructor(
        override val id: PlayerId.UserId,
        override val status: PlayerStatus,
    ) : Player {

        companion object {
            fun create() = User(
                id = PlayerId.UserId(UUID.randomUUID().toString()),
                status = PlayerStatus.WaitMatting,
            )
            fun reconstruct(
                id: PlayerId.UserId,
                status: PlayerStatus,
            ) = User(id = id, status = status)
        }
    }
}

sealed interface PlayerId {
    val value: String
    class AiId(override val value: String) : PlayerId
    class UserId(override val value: String) : PlayerId
}

sealed interface PlayerStatus {
    object WaitMatting : PlayerStatus
    data class OnMatch(val roomId: RoomId) : PlayerStatus
}
