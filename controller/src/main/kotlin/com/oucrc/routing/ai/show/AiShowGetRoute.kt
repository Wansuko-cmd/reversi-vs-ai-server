package com.oucrc.routing.ai.show

import com.oucrc.ext.getParameter
import com.oucrc.serializable.ExceptionSerializable
import com.oucrc.serializable.PlayerSerializable
import com.wsr.result.consume
import com.wsr.result.flatMap
import com.wsr.result.mapBoth
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import org.koin.ktor.ext.inject
import player.PlayerId
import player.ai.GetAiByIdUseCase

fun Route.aiShowGet(path: String, param: String) {
    val getAiByIdUseCase by inject<GetAiByIdUseCase>()

    get(path) {
        call.getParameter<String>(param)
            .flatMap { id -> getAiByIdUseCase(PlayerId.AiId(id)) }
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
