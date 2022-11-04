package player

data class PlayerUseCaseModel(
    val id: PlayerId,
    val name: PlayerName,
    val status: PlayerStatus,
) {
    companion object {
        fun from(player: Player): PlayerUseCaseModel =
            PlayerUseCaseModel(
                id = player.id,
                name = player.name,
                status = player.status,
            )
    }
}
