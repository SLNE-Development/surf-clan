package dev.slne.surf.clan.core.client

import dev.slne.surf.clan.core.ClanInstance
import dev.slne.surf.clan.core.client.rabbit.RabbitService
import dev.slne.surf.clan.core.client.redis.RedisService

abstract class ClientClanInstance : ClanInstance {

    override suspend fun load() {
        super.load()

        RabbitService.connect()
        RedisService.get().connect()
    }

    override suspend fun disable() {
        super.disable()

        RedisService.get().disconnect()
        RabbitService.disconnect()
    }
}