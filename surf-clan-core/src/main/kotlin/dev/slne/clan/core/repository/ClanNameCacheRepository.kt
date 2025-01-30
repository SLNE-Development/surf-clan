package dev.slne.clan.core.repository

import dev.slne.clan.core.name.CoreNameCache
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ClanNameCacheRepository : JpaRepository<CoreNameCache, Long> {

    fun findFirstByUuid(uuid: UUID): CoreNameCache?

    fun findFirstByUsername(username: String): CoreNameCache?

}