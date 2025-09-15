package dev.slne.surf.clan.api.common.clan.member.role.permission

import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import net.kyori.adventure.text.Component

enum class ClanPermission(val failedMessage: SurfComponentBuilder.(ClanPlayer) -> Unit) {
    DISBAND({ player ->
        error("Du hast keine Berechtigung, um den Clan aufzulösen.")
    }),

    MEMBER_INVITE({ player ->
        error("Du hast keine Berechtigung, um Mitglieder einzuladen.")
    }),

    MEMBER_REMOVE({ player ->
        error("Du hast keine Berechtigung, um Mitglieder zu entfernen.")
    }),

    MEMBER_PROMOTE({ player ->
        error("Du hast keine Berechtigung, um Mitglieder heraufzustufen.")
    }),

    MEMBER_DEMOTE({ player ->
        error("Du hast keine Berechtigung, um Mitglieder herabzustufen.")
    }),

    OPTIONS_DISCORD({ player ->
        error("Du hast keine Berechtigung, um den Discord-Einladungslink zu ändern.")
    }),

    OPTIONS_TAG_COLOR({ player ->
        error("Du hast keine Berechtigung, um die Tag-Farbe zu ändern.")
    }),

    OPTIONS_TAG_TAG({ player ->
        error("Du hast keine Berechtigung, um den Tag zu ändern.")
    }),

    OPTIONS_NAME({ player ->
        error("Du hast keine Berechtigung, um den Namen zu ändern.")
    });

    fun asComponent(player: ClanPlayer): Component {
        val builder = SurfComponentBuilder.builder()
        failedMessage(builder, player)
        return builder.build()
    }
}