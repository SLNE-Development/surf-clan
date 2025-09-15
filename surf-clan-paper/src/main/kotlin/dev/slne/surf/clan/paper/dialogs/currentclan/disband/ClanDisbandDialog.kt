@file:Suppress("UnstableApiUsage")

package dev.slne.surf.clan.paper.dialogs.currentclan.disband

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.paper.dialogs.appendClanDialogTitle
import dev.slne.surf.clan.paper.dialogs.currentclan.disband.buttons.createClanDisbandConfirmButton
import dev.slne.surf.clan.paper.dialogs.currentclan.disband.buttons.createClanDisbandDenyButton
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import io.papermc.paper.dialog.Dialog

object ClanDisbandDialog

fun ClanDisbandDialog.createDialog(
    selfClanPlayer: ClanPlayer,
    clanPlayer: ClanPlayer,
    clan: Clan
): Dialog = dialog {

    base {
        title { appendClanDialogTitle(buildText { variableValue(clan.name) }, buildText { warning("Clan auflösen") }) }
        body {
            plainMessage {
                warning("Du bist dabei deinen Clan unwiderruflich aufzulösen!")
                appendNewline(2)
                warning("Bitte bestätige deine Entscheidung.")
            }
        }
    }

    type {
        confirmation(
            ClanDisbandDialog.createClanDisbandDenyButton(selfClanPlayer, clanPlayer, clan),
            ClanDisbandDialog.createClanDisbandConfirmButton(selfClanPlayer, clanPlayer, clan)
        )
    }
}