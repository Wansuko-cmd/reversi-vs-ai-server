package room

import Board
import Cell
import player.PlayerUseCaseModel

data class RoomUseCaseModel(
    val id: RoomId,
    val black: PlayerUseCaseModel,
    val white: PlayerUseCaseModel,
    val next: PlayerUseCaseModel?,
    val board: BoardUseCaseModel,
) {
    companion object {
        fun from(room: Room): RoomUseCaseModel =
            RoomUseCaseModel(
                id = room.id,
                black = PlayerUseCaseModel.from(room.black),
                white = PlayerUseCaseModel.from(room.white),
                next = when (room.next) {
                    is Cell.Piece.Black -> PlayerUseCaseModel.from(room.black)
                    is Cell.Piece.White -> PlayerUseCaseModel.from(room.white)
                    else -> null
                },
                board = BoardUseCaseModel.from(room.board),
            )
    }
}

data class BoardUseCaseModel(
    val columns: List<List<Cell>>
) {
    companion object {
        fun from(board: Board): BoardUseCaseModel =
            (0 until board.height).map { row ->
                (0 until board.width).map { column ->
                    board[board.Coordinate(row, column)]
                }
            }.let { BoardUseCaseModel(it) }
    }
}
