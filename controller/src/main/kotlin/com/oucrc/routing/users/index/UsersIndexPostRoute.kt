package com.oucrc.routing.users.index

import com.oucrc.serializable.ExceptionSerializable
import com.oucrc.serializable.PlayerSerializable
import com.wsr.result.consume
import com.wsr.result.mapBoth
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import org.koin.ktor.ext.inject
import player.user.CreateUserUseCase

fun Route.usersIndexPost(path: String) {
    val createUserUseCase by inject<CreateUserUseCase>()

    post(path) {
        createUserUseCase()
            .mapBoth(
                success = { user -> PlayerSerializable.from(user) },
                failure = { ExceptionSerializable.from(it) }
            )
            .consume(
                success = { user -> call.respond(user) },
                failure = { (message, status) -> call.respond(status, message) }
            )
    }
}
