package player

sealed interface Player {
    val id: PlayerId
    class Ai(override val id: PlayerId) : Player
    class User(override val id: PlayerId) : Player
}

@JvmInline
value class PlayerId(val value: String)
