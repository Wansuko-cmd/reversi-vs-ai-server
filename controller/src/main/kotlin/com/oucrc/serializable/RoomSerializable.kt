package com.oucrc.serializable

import room.RoomUseCaseModel

@kotlinx.serialization.Serializable
data class RoomSerializable(
    val id: String,
    val black: PlayerSerializable,
    val white: PlayerSerializable,
    val next: PlayerSerializable?,
    val board: List<List<Int>>,
) {
    companion object {
        fun from(roomUseCaseModel: RoomUseCaseModel) =
            RoomSerializable(
                id = roomUseCaseModel.id.value,
                black = PlayerSerializable.from(roomUseCaseModel.black),
                white = PlayerSerializable.from(roomUseCaseModel.white),
                next = roomUseCaseModel.next?.let { PlayerSerializable.from(it) },
                board = boardSerializable(roomUseCaseModel.board),
            )
    }
}
