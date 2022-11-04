package com.oucrc.routing.rooms.index

import com.oucrc.ext.getRequest
import com.oucrc.serializable.ExceptionSerializable
import com.oucrc.serializable.RoomSerializable
import com.wsr.result.consume
import com.wsr.result.flatMap
import com.wsr.result.mapBoth
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject
import player.PlayerId
import room.CreateRoomUseCase

fun Route.roomsIndexPost(path: String) {
    val createRoomUseCase by inject<CreateRoomUseCase>()

    post(path) {
        call.getRequest<RoomsIndexPostRequest>("Invalid request.")
            .flatMap { (aiId, userId,) ->
                createRoomUseCase(aiId = PlayerId.AiId(aiId), userId = PlayerId.UserId(userId))
            }
            .mapBoth(
                success = { room -> RoomSerializable.from(room) },
                failure = { ExceptionSerializable.from(it) },
            )
            .consume(
                success = { room -> call.respond(room) },
                failure = { (message, status) -> call.respond(status, message) }
            )
    }
}

@Serializable
data class RoomsIndexPostRequest(
    @SerialName("ai_id") val aiId: String,
    @SerialName("user_id") val userId: String,
)
