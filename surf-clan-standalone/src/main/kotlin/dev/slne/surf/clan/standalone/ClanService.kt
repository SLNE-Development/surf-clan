package dev.slne.surf.clan.standalone

import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.results.ClanCreateResult
import dev.slne.surf.clan.core.common.ClanImpl
import dev.slne.surf.clan.core.common.netty.packets.server.ServerboundCreateClanPacket
import dev.slne.surf.clan.standalone.entities.ClanEntity
import dev.slne.surf.cloud.api.common.util.freeze
import dev.slne.surf.cloud.api.common.util.mutableObjectSetOf
import dev.slne.surf.cloud.api.server.plugin.CoroutineTransactional
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import java.util.*

@Service
@CoroutineTransactional
class ClanService {

    private val _clans = mutableObjectSetOf<ClanImpl>()
    val clans get() = _clans.freeze()

    @PostConstruct
    suspend fun fetchClans() {
        clans.clear()
        clans.addAll(ClanEntity.all().map { it.toClanImpl() })
    }

    fun findClanByName(name: String) = clans.find { it.name == name }

    fun findClanByTag(tag: String) = clans.find { it.tag == tag }

    fun findClanByPlayer(clanPlayer: ClanPlayer) =
        clans.find { it.members.any { member -> member.uuid == clanPlayer.uuid } }

    fun generateClanUuid(): UUID {
        var clanUuid = UUID.randomUUID()

        while (clans.any { it.uuid == clanUuid }) {
            clanUuid = UUID.randomUUID()
        }

        return clanUuid
    }

    suspend fun createClan(
        name: String,
        tag: String,
        createdBy: ClanPlayer
    ): Pair<ClanCreateResult, ClanImpl?> {
        if (clans.any { it.name == name }) {
            return ClanCreateResult.NAME_TAKEN to null
        }

        if (clans.any { it.tag == tag }) {
            return ClanCreateResult.TAG_TAKEN to null
        }

        if (name.length < 3 || name.length > 16) {
            return ClanCreateResult.NAME_LENGTH to null
        }

        if (tag.length < 3 || tag.length > 4) {
            return ClanCreateResult.TAG_LENGTH to null
        }

        if (findClanByPlayer(createdBy) != null) {
            return ClanCreateResult.ALREADY_IN_CLAN to null
        }

        val clan = ClanEntity.new {
            this.uuid = generateClanUuid()
            this.name = name
            this.tag = tag
            this.createdBy = createdBy.uuid
        }.toClanImpl()

        clans.add(clan)

        return ClanCreateResult.SUCCESS to clan
    }

    suspend fun createClan(packet: ServerboundCreateClanPacket) =
        createClan(packet.name, packet.tag, packet.createdBy)

}