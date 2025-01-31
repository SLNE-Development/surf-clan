package dev.slne.clan.core.repository

import dev.slne.clan.core.player.CoreClanPlayer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ClanPlayerRepository : JpaRepository<CoreClanPlayer, Long> {

    fun findFirstByUuid(uuid: UUID): CoreClanPlayer?

    fun findFirstByUsername(username: String): CoreClanPlayer?

}