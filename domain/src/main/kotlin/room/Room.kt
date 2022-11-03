package room

import Board
import DomainException
import com.wsr.result.ApiResult
import player.Player
import player.PlayerId
import java.util.UUID

class Room private constructor(
    val id: RoomId,
    val black: Player,
    val white: Player,
    val board: Board,
    val next: Cell.Piece?,
) {
    fun isNextUser(playerId: PlayerId): Boolean = when (next) {
        is Cell.Piece.Black -> playerId == black.id
        is Cell.Piece.White -> playerId == white.id
        else -> false
    }

    fun place(row: Int, column: Int): ApiResult<Room, DomainException> {
        if (next == null) {
            return ApiResult.Failure(DomainException.FinishedGameException("This is finished game."))
        }
        val coordinate = board.Coordinate(row, column)
        if (!board.isPlaceable(coordinate, next)) {
            return ApiResult.Failure(
                DomainException.NotPlaceableCoordinateException(
                    message = "row : $row, column : $column isn't placeable.",
                ),
            )
        }

        val placedBoard = board.place(coordinate, next)
        val next = when {
            placedBoard.placeableCoordinates(next.reverse()).isNotEmpty() -> next.reverse()
            placedBoard.placeableCoordinates(next).isNotEmpty() -> next
            else -> null
        }
        return Room(
            id = this.id,
            black = black,
            white = white,
            next = next,
            board = placedBoard
        ).let { ApiResult.Success(it) }
    }

    companion object {
        fun create(ai: Player.Ai, user: Player.User): Room {
            val (black, white) = (ai to user).randomSwap()
            return Room(
                id = RoomId(UUID.randomUUID().toString()),
                black = black,
                white = white,
                board = Board.create(8),
                next = Cell.Piece.Black,
            )
        }

        private fun <T> Pair<T, T>.randomSwap() =
            (0..1)
                .random()
                .let { if (it == 0) first to second else second to first }

        fun reconstruct(
            id: RoomId,
            black: Player,
            white: Player,
            board: Board,
            next: Cell.Piece?,
        ) = Room(
            id = id,
            black = black,
            white = white,
            next = next,
            board = board,
        )
    }
}

@JvmInline
value class RoomId(val value: String)
