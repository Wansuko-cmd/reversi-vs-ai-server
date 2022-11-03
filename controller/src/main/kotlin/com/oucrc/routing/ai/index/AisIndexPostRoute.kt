package com.oucrc.routing.ai.index

import com.oucrc.serializable.ExceptionSerializable
import com.oucrc.serializable.PlayerSerializable
import com.wsr.result.consume
import com.wsr.result.mapBoth
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import org.koin.ktor.ext.inject
import player.ai.CreateAiUseCase

fun Route.aisIndexPost(path: String) {
    val createAiUseCase by inject<CreateAiUseCase>()

    post(path) {
        createAiUseCase()
            .mapBoth(
                success = { ai -> PlayerSerializable.from(ai) },
                failure = { ExceptionSerializable.from(it) },
            )
            .consume(
                success = { ai -> call.respond(ai) },
                failure = { (message, status) -> call.respond(status, message) }
            )
    }
}
