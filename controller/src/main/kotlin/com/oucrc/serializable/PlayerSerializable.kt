package com.oucrc.serializable

import player.PlayerStatus
import player.PlayerUseCaseModel

@kotlinx.serialization.Serializable
data class PlayerSerializable(
    val id: String,
    val status: String?,
) {
    companion object {
        fun from(player: PlayerUseCaseModel) =
            PlayerSerializable(
                id = player.id.value,
                status = when (val status = player.status) {
                    is PlayerStatus.WaitMatting -> null
                    is PlayerStatus.OnMatch -> status.roomId.value
                }
            )
    }
}
