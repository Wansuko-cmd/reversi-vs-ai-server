package player

import room.RoomId
import java.util.UUID

sealed interface Player {
    val id: PlayerId
    val name: PlayerName
    val status: PlayerStatus
    fun updateStatus(status: PlayerStatus): Player = when (this) {
        is Ai -> Ai.reconstruct(
            id = id,
            name = name,
            status = status,
        )
        is User -> User.reconstruct(
            id = id,
            name = name,
            status = status,
        )
    }
    class Ai private constructor(
        override val id: PlayerId.AiId,
        override val name: PlayerName.AiName,
        override val status: PlayerStatus,
    ) : Player {

        companion object {
            fun create(name: PlayerName.AiName) = Ai(
                id = PlayerId.AiId(UUID.randomUUID().toString()),
                name = name,
                status = PlayerStatus.WaitMatting,
            )
            fun reconstruct(
                id: PlayerId.AiId,
                name: PlayerName.AiName,
                status: PlayerStatus,
            ) = Ai(id = id, name = name, status = status)
        }
    }
    class User private constructor(
        override val id: PlayerId.UserId,
        override val name: PlayerName.UserName,
        override val status: PlayerStatus,
    ) : Player {

        companion object {
            fun create(name: PlayerName.UserName) = User(
                id = PlayerId.UserId(UUID.randomUUID().toString()),
                name = name,
                status = PlayerStatus.WaitMatting,
            )
            fun reconstruct(
                id: PlayerId.UserId,
                name: PlayerName.UserName,
                status: PlayerStatus,
            ) = User(id = id, name = name, status = status)
        }
    }
}

sealed interface PlayerId {
    val value: String
    class AiId(override val value: String) : PlayerId
    class UserId(override val value: String) : PlayerId
}

sealed interface PlayerName {
    val value: String
    class AiName(override val value: String = "AI") : PlayerName
    class UserName(override val value: String) : PlayerName
}

sealed interface PlayerStatus {
    object WaitMatting : PlayerStatus
    data class OnMatch(val roomId: RoomId) : PlayerStatus
}
