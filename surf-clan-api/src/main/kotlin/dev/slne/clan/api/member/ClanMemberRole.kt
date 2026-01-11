package dev.slne.clan.api.member

import dev.slne.clan.api.permission.ClanPermission
import dev.slne.surf.surfapi.core.api.messages.Colors
import dev.slne.surf.surfapi.core.api.messages.adventure.text
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import java.util.*

enum class ClanMemberRole(
    val displayName: Component,
    vararg permissions: ClanPermission,
): ComponentLike {
    MEMBER(
        text("Mitglied", Colors.YELLOW)
    ),
    OFFICER(
        text("Offizier", Colors.GOLD),
        ClanPermission.INVITE,
        ClanPermission.KICK,
    ),
    LEADER(
        text("Anführer", Colors.RED),
        ClanPermission.INVITE,
        ClanPermission.KICK,
        ClanPermission.DEMOTE,
        ClanPermission.PROMOTE,
        ClanPermission.DISCORD
    ),
    OWNER(
        text("Besitzer", Colors.RED),
        ClanPermission.DISBAND,
        ClanPermission.INVITE,
        ClanPermission.KICK,
        ClanPermission.DEMOTE,
        ClanPermission.PROMOTE,
        ClanPermission.DISCORD,
        ClanPermission.OPTIONS_TAG_COLOR
    );

    private val permissions = EnumSet.copyOf(permissions.toSet())

    fun hasNextRole() = this != LEADER && this != OWNER
    fun hasPreviousRole() = this != MEMBER

    fun nextRole() = when (this) {
        MEMBER -> OFFICER
        OFFICER -> LEADER
        LEADER -> LEADER
        OWNER -> OWNER
    }

    fun previousRole() = when (this) {
        MEMBER -> MEMBER
        OFFICER -> MEMBER
        LEADER -> OFFICER
        OWNER -> OWNER
    }

    fun hasPermission(permission: ClanPermission) = permissions.contains(permission)

    override fun asComponent() = displayName
}