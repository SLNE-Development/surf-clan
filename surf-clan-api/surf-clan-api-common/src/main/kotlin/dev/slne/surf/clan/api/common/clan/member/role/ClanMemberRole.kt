package dev.slne.surf.clan.api.common.clan.member.role

import dev.slne.surf.clan.api.common.clan.member.role.permission.ClanPermission
import dev.slne.surf.surfapi.core.api.messages.Colors
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import net.kyori.adventure.text.ComponentLike

enum class ClanMemberRole(
    private val permissions: List<ClanPermission>,
    val displayName: SurfComponentBuilder.() -> Unit,
) : ComponentLike {

    MEMBER(
        displayName = {
            text("Mitglied", Colors.YELLOW)
        },
        permissions = listOf()
    ),
    OFFICER(
        displayName = {
            text("Offizier", Colors.GOLD)
        },
        permissions = listOf(
            ClanPermission.INVITE,
            ClanPermission.KICK,
        ),
    ),
    LEADER(
        displayName = {
            text("Anführer", Colors.RED)
        },
        permissions = listOf(
            ClanPermission.INVITE,
            ClanPermission.KICK,

            ClanPermission.DEMOTE,
            ClanPermission.PROMOTE,

            ClanPermission.OPTIONS_DISCORD
        ),
    ),
    OWNER(
        displayName = {
            text("Besitzer", Colors.RED)
        },
        permissions = listOf(
            ClanPermission.DISBAND,

            ClanPermission.INVITE,
            ClanPermission.KICK,

            ClanPermission.DEMOTE,
            ClanPermission.PROMOTE,

            ClanPermission.OPTIONS_DISCORD,
            ClanPermission.OPTIONS_NAME,
            ClanPermission.OPTIONS_TAG,
            ClanPermission.OPTIONS_TAG_COLOR
        )
    );

    override fun asComponent() = buildText(displayName)

    val hasNextRole get() = this != LEADER
    val hasPreviousRole get() = this != MEMBER

    val isOwner get() = this == OWNER

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

    fun hasPermission(permission: ClanPermission) =
        permissions.contains(permission)

}