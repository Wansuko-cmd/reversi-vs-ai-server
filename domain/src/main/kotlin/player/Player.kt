package player

import java.util.UUID

sealed interface Player {
    val id: PlayerId
    class Ai private constructor(override val id: PlayerId.AiId) : Player {
        companion object {
            fun create() = Ai(id = PlayerId.AiId(UUID.randomUUID().toString()))
            fun reconstruct(id: PlayerId.AiId) = Ai(id = id)
        }
    }
    class User private constructor(override val id: PlayerId.UserId) : Player {
        companion object {
            fun create() = User(id = PlayerId.UserId(UUID.randomUUID().toString()))
            fun reconstruct(id: PlayerId.UserId) = User(id = id)
        }
    }
}

sealed interface PlayerId {
    val value: String
    class AiId(override val value: String) : PlayerId
    class UserId(override val value: String) : PlayerId
}
