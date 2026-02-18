package dev.slne.clan.paper.redis

import com.google.auto.service.AutoService
import dev.slne.clan.core.redis.RedisService

@AutoService(RedisService::class)
class PaperRedisService : RedisService() {
}