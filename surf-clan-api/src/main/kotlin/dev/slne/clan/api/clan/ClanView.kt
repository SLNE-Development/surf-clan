package dev.slne.clan.api.clan

import dev.slne.clan.api.invite.ClanInviteView
import dev.slne.clan.api.member.ClanMemberView
import dev.slne.clan.api.permission.ClanPermission
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import java.time.OffsetDateTime
import java.util.*

interface ClanView {
    val uuid: UUID
    val name: String
    val tag: String
    val createdByUuid: UUID

    val description: String?
    val discordInvite: String?
    val clanTagColor: TextColor

    val members: Set<ClanMemberView>

    val updatedAt: OffsetDateTime
    val createdAt: OffsetDateTime

    suspend fun getPendingInvites(): Set<ClanInviteView>

    fun isMember(uuid: UUID): Boolean
    fun hasMemberPermission(uuid: UUID, permission: ClanPermission): Boolean
    fun getMember(uuid: UUID): ClanMemberView?

    fun getRichClanTag(): Component
    suspend fun renderClanTag(minSize: Int = 0): Component
}