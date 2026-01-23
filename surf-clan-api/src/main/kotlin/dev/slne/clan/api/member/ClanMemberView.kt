package dev.slne.clan.api.member

import dev.slne.clan.api.permission.ClanPermission
import org.jetbrains.annotations.ApiStatus
import java.util.*

@ApiStatus.NonExtendable
interface ClanMemberView {
    val uuid: UUID
    val role: ClanMemberRole
    val addedBy: UUID?

    fun hasPermission(clanPermission: ClanPermission): Boolean
}