package dev.slne.clan.core.repository

import dev.slne.clan.core.CoreClan
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ClanRepository : JpaRepository<CoreClan, Long> {

    fun findFirstByUuid(uuid: UUID): CoreClan?

    fun findFirstByTagIgnoreCase(tag: String): CoreClan?

    fun findFirstByNameIgnoreCase(name: String): CoreClan?

    @Query("SELECT c FROM CoreClan c JOIN c._members m WHERE m.uuid = :uuid")
    fun findByMemberUuid(@Param("uuid") uuid: UUID): CoreClan?

    fun existsByUuid(uuid: UUID): Boolean

}