package com.oucrc.serializable

import player.PlayerUseCaseModel

@kotlinx.serialization.Serializable
data class PlayerSerializable(
    val id: String,
) {
    companion object {
        fun from(player: PlayerUseCaseModel) =
            PlayerSerializable(id = player.id.value)
    }
}
