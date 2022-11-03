package player

sealed interface Player {
    val id: PlayerId
    class Ai(override val id: PlayerId.AiId) : Player
    class User(override val id: PlayerId.UserId) : Player
}

sealed interface PlayerId {
    val value: String
    class AiId(override val value: String) : PlayerId
    class UserId(override val value: String) : PlayerId
}
