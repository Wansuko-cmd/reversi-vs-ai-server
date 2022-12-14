package com.oucrc.routing.rooms.show

import com.oucrc.ext.getParameter
import com.oucrc.ext.getRequest
import com.oucrc.serializable.ExceptionSerializable
import com.wsr.result.consume
import com.wsr.result.flatMap
import com.wsr.result.map
import com.wsr.result.mapFailure
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import kotlinx.serialization.SerialName
import org.koin.ktor.ext.inject
import player.PlayerId
import room.PlacePieceInRoomUseCase
import room.RoomId

fun Route.roomsShowPost(path: String, param: String) {
    val placePieceInRoomUseCase by inject<PlacePieceInRoomUseCase>()
    post(path) {
        call.getParameter<String>(param, errorMessage = "Invalid room id.")
            .flatMap { id ->
                call.getRequest<RoomShowPostRequest>(errorMessage = "Invalid request.")
                    .map { request ->
                        placePieceInRoomUseCase(
                            RoomId(id),
                            request.row,
                            request.column,
                            if (request.isUser) PlayerId.UserId(request.userId) else PlayerId.AiId(request.userId)
                        )
                    }
            }
            .mapFailure { ExceptionSerializable.from(it) }
            .consume(
                success = { call.respond(HttpStatusCode.OK) },
                failure = { (message, status) -> call.respond(status, message) }
            )
    }
}

@kotlinx.serialization.Serializable
data class RoomShowPostRequest(
    @SerialName("user_id") val userId: String,
    @SerialName("is_user") val isUser: Boolean = false,
    val row: Int,
    val column: Int,
)
