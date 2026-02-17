package dev.slne.clan.velocity.redis

import com.google.auto.service.AutoService
import dev.slne.clan.core.redis.RedisService
import dev.slne.clan.velocity.redis.listener.ClanRedisListener

@AutoService(RedisService::class)
class VelocityRedisService : RedisService() {
    override fun register() {
        super.register()

        redisApi.subscribeToEvents(ClanRedisListener)
    }
}