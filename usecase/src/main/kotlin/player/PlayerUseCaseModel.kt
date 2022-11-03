package player

data class PlayerUseCaseModel(
    val id: PlayerId,
) {
    companion object {
        fun from(player: Player): PlayerUseCaseModel =
            PlayerUseCaseModel(
                id = player.id,
            )
    }
}
