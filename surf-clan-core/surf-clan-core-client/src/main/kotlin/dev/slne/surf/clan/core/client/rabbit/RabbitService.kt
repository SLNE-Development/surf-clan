package dev.slne.surf.clan.core.client.rabbit

import dev.slne.surf.clan.core.ClanCoreSerializerModule
import dev.slne.surf.clan.core.ClanInstance
import dev.slne.surf.rabbitmq.api.ClientRabbitMQApi

object RabbitService {
    val rabbitApi = ClientRabbitMQApi.create(
        "surf-clan",
        ClanInstance.dataPath,
        ClanCoreSerializerModule.module
    )

    suspend fun connect() {
        rabbitApi.freezeAndConnect()
    }

    suspend fun disconnect() {
        rabbitApi.disconnect()
    }
}

val rabbitApi get() = RabbitService.rabbitApi