package com.oucrc.routing.ai.index

import com.oucrc.serializable.ExceptionSerializable
import com.oucrc.serializable.PlayerSerializable
import com.wsr.result.consume
import com.wsr.result.mapBoth
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import org.koin.ktor.ext.inject
import player.GetAisUseCase

fun Route.aisIndexGet(path: String) {
    val getAisUseCase by inject<GetAisUseCase>()

    get(path) {
        getAisUseCase()
            .mapBoth(
                success = { ais -> ais.map { PlayerSerializable.from(it) } },
                failure = { ExceptionSerializable.from(it) },
            )
            .consume(
                success = { ais -> call.respond(ais) },
                failure = { (message, status) -> call.respond(status, message) },
            )
    }
}
