package com.oucrc.plugins

import db.DevDB
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import player.AiRepository
import player.UserRepository
import player.ai.CreateAiUseCase
import player.ai.GetAiByIdUseCase
import player.ai.GetAisUseCase
import player.user.CreateUserUseCase
import player.user.GetUserByIdUseCase
import player.user.GetUsersUseCase
import repository.AiRepositoryImpl
import repository.RoomRepositoryImpl
import repository.UserRepositoryImpl
import room.CreateRoomUseCase
import room.GetRoomByIdUseCase
import room.GetRoomsUseCase
import room.PlacePieceInRoomUseCase
import room.RoomRepository

fun Application.koinPlugins() {
    val module = module {
        /*** UseCase ***/
        // Room
        single<CreateRoomUseCase> { CreateRoomUseCase(get(), get(), get()) }
        single<GetRoomsUseCase> { GetRoomsUseCase(get()) }
        single<GetRoomByIdUseCase> { GetRoomByIdUseCase(get()) }
        single<PlacePieceInRoomUseCase> { PlacePieceInRoomUseCase(get()) }

        // Ai
        single<CreateAiUseCase> { CreateAiUseCase(get()) }
        single<GetAisUseCase> { GetAisUseCase(get()) }
        single<GetAiByIdUseCase> { GetAiByIdUseCase(get()) }

        // User
        single<CreateUserUseCase> { CreateUserUseCase(get()) }
        single<GetUsersUseCase> { GetUsersUseCase(get()) }
        single<GetUserByIdUseCase> { GetUserByIdUseCase(get()) }

        /*** Repository ***/
        single<RoomRepository> { RoomRepositoryImpl(get()) }
        single<UserRepository> { UserRepositoryImpl(get()) }
        single<AiRepository> { AiRepositoryImpl(get()) }

        /*** Database ***/
        single<Database> { DevDB }
    }

    install(Koin) { modules(module) }
}
