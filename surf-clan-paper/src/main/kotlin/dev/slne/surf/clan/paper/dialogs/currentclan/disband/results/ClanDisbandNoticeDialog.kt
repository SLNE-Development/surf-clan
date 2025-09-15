@file:Suppress("UnstableApiUsage")

package dev.slne.surf.clan.paper.dialogs.currentclan.disband.results

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.paper.dialogs.appendClanDialogTitle
import dev.slne.surf.clan.paper.dialogs.currentclan.disband.ClanDisbandDialog
import dev.slne.surf.clan.paper.dialogs.currentclan.disband.buttons.createClanDisbandErrorNoticeButton
import dev.slne.surf.clan.paper.dialogs.currentclan.disband.buttons.createClanDisbandSuccessNoticeButton
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import io.papermc.paper.dialog.Dialog

fun ClanDisbandDialog.createClanDisbandNoticeDialog(
    isSuccess: Boolean,
    selfClanPlayer: ClanPlayer,
    clanPlayer: ClanPlayer,
    clan: Clan
): Dialog = dialog {

    base {
        title { appendClanDialogTitle(buildText { variableValue(clan.name) }, buildText { warning("Clan auflösen") }) }
        body {
            plainMessage {
                if (isSuccess) {
                    success("Du hast deinen Clan erfolgreich aufgelöst.")
                } else {
                    warning("Es ist ein Fehler aufgetreten.")
                    appendNewline(2)
                    warning("Bitte versuche es später erneut.")
                }
            }
        }
    }
    type {
        notice {
            if (isSuccess) {
                ClanDisbandDialog.createClanDisbandSuccessNoticeButton(selfClanPlayer, clanPlayer)
            } else {
                ClanDisbandDialog.createClanDisbandErrorNoticeButton(selfClanPlayer, clanPlayer, clan)
            }
        }
    }
}