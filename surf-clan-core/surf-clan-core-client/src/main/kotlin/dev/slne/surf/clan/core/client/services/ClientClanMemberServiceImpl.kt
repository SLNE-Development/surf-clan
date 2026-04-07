package dev.slne.surf.clan.core.client.services

import com.github.benmanes.caffeine.cache.Caffeine
import com.google.auto.service.AutoService
import dev.slne.clan.api.member.ClanMember
import dev.slne.clan.api.member.ClanMemberAddResult
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.clan.api.member.ClanMemberService
import dev.slne.clan.api.member.listener.ClanMemberChangedRoleListener
import dev.slne.clan.api.member.listener.ClanMemberListener
import dev.slne.surf.clan.core.client.rabbit.rabbitApi
import dev.slne.surf.clan.core.member.ClanMemberImpl
import dev.slne.surf.clan.core.member.CoreClanMemberService
import dev.slne.surf.clan.core.protocol.member.changeRole.ChangeClanMemberRoleRequestPacket
import dev.slne.surf.clan.core.protocol.member.create.CreateClanMemberRequestPacket
import dev.slne.surf.clan.core.protocol.member.delete.DeleteClanMemberRequestPacket
import dev.slne.surf.clan.core.protocol.member.findByUuid.FindClanMemberByUuidRequestPacket
import dev.slne.surf.surfapi.core.api.service.PlayerLookupService
import dev.slne.surf.surfapi.core.api.util.logger
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

@AutoService(ClanMemberService::class)
class ClientClanMemberServiceImpl : CoreClanMemberService {
    private val loadedMembers = Caffeine.newBuilder()
        .weakValues()
        .maximumSize(10_000)
        .build<UUID, ClanMemberImpl>()

    private val listeners = CopyOnWriteArrayList<ClanMemberListener>()

    override fun registerListener(listener: ClanMemberListener) {
        listeners.add(listener)
    }

    override fun unregisterListener(listener: ClanMemberListener) {
        listeners.remove(listener)
    }

    private inline fun <reified T : ClanMemberListener> getListeners(): List<T> {
        return listeners.filterIsInstance<T>()
    }

    private inline fun invokeListenerSafe(block: () -> Unit) {
        try {
            block()
        } catch (e: Throwable) {
            log.atWarning()
                .withCause(e)
                .log("Failed to invoke clan member listener")
        }
    }

    private inline fun <reified T : ClanMemberListener> callListeners(call: (T) -> Unit) {
        for (listener in getListeners<T>()) {
            invokeListenerSafe { call(listener) }
        }
    }

    fun callClanMemberChangedRoleListeners(member: ClanMember, role: ClanMemberRole) {
        callListeners<ClanMemberChangedRoleListener> { it.onClanMemberChangedRole(member, role) }
    }

    override suspend fun findMemberByName(name: String): ClanMember? {
        val uuid = PlayerLookupService.getUuid(name) ?: return null
        return findMemberByUuid(uuid)
    }

    override suspend fun findMemberByUuid(uuid: UUID): ClanMember? {
        val member = loadedMembers.getIfPresent(uuid)
        if (member != null) return member

        val request = FindClanMemberByUuidRequestPacket(uuid)
        val loaded = rabbitApi.sendRequest(request).member ?: return null
        loadedMembers.put(uuid, loaded)

        return loaded
    }

    override suspend fun addMember(
        clanID: ULong,
        player: UUID,
        role: ClanMemberRole,
        invitedBy: UUID?
    ): ClanMemberAddResult {
        val request = CreateClanMemberRequestPacket(clanID, player, role, invitedBy)
        val result = rabbitApi.sendRequest(request).result

        if (result is ClanMemberAddResult.Success) {
            ClientClanServiceImpl.get().invalidateCachedClanByID(clanID)
        }

        return result
    }

    override suspend fun removeMember(clanID: ULong, player: UUID): Boolean {
        val request = DeleteClanMemberRequestPacket(clanID, player)
        val result = rabbitApi.sendRequest(request).value

        if (result) {
            ClientClanServiceImpl.get().invalidateCachedClanByID(clanID)
        }

        return result
    }

    override suspend fun changeRole(
        member: ClanMemberImpl,
        role: ClanMemberRole
    ): Boolean {
        val request = ChangeClanMemberRoleRequestPacket(member.ID, role)
        val changed = rabbitApi.sendRequest(request).value

        if (changed) {
            ClientClanServiceImpl.get().invalidateCachedClanByMember(member.uuid)
            member.role = role
            loadedMembers.getIfPresent(member.uuid)?.let {
                it.role = role
            }

            callClanMemberChangedRoleListeners(member, role)
        }

        return changed
    }

    companion object {
        private val log = logger()

        fun get() = ClanMemberService.INSTANCE as ClientClanMemberServiceImpl
    }
}