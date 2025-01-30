package dev.slne.clan.api.member

import dev.slne.clan.api.permission.ClanPermission
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

enum class ClanMemberRole(
    private val permissions: Array<ClanPermission>,
    val displayName: Component,
) {

    MEMBER(
        arrayOf(),
        Component.text("Mitglied", NamedTextColor.YELLOW)
    ),
    OFFICER(
        arrayOf(
            ClanPermission.INVITE,
            ClanPermission.KICK,
        ),
        Component.text("Offizier", NamedTextColor.GOLD)
    ),
    LEADER(
        arrayOf(
            ClanPermission.INVITE,
            ClanPermission.KICK,

            ClanPermission.DEMOTE,
            ClanPermission.PROMOTE,

            ClanPermission.DISBAND
        ),
        Component.text("Anführer", NamedTextColor.RED)
    );

    fun hasNextRole() = this != LEADER
    fun hasPreviousRole() = this != MEMBER

    fun nextRole() = when (this) {
        MEMBER -> OFFICER
        OFFICER -> LEADER
        LEADER -> LEADER
    }

    fun previousRole() = when (this) {
        MEMBER -> MEMBER
        OFFICER -> MEMBER
        LEADER -> OFFICER
    }

    fun hasPermission(permission: ClanPermission) =
        ObjectOpenHashSet(permissions).contains(permission)

}