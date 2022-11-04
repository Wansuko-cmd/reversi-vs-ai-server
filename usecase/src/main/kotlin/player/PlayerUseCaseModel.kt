package player

data class PlayerUseCaseModel(
    val id: PlayerId,
    val status: PlayerStatus,
) {
    companion object {
        fun from(player: Player): PlayerUseCaseModel =
            PlayerUseCaseModel(
                id = player.id,
                status = player.status,
            )
    }
}
