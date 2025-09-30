@file:Suppress("UnstableApiUsage")

package dev.slne.surf.clan.paper.dialogs.create.results

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.paper.dialogs.create.CreateClanDialog
import dev.slne.surf.clan.paper.dialogs.create.createDialog
import dev.slne.surf.clan.paper.dialogs.currentclan.CurrentClanDialog
import dev.slne.surf.clan.paper.dialogs.currentclan.createDialog
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import net.kyori.adventure.text.ComponentLike

fun CreateClanDialog.createClanCreationNoticeDialog(
    selfClanPlayer: ClanPlayer,
    clanPlayer: ClanPlayer,
    clan: Clan?,
    name: String,
    tag: String,
    errors: List<ComponentLike>
) = dialog {
    base {
        body {
            plainMessage {
                if (errors.isEmpty()) {
                    success("Dein Clan wurde erfolgreich erstellt!")
                } else {
                    error("Dein Clan konnte nicht erstellt werden, da folgende Fehler aufgetreten sind:")
                    appendNewline(2)

                    errors.forEachIndexed { index, error ->
                        if (index > 0) {
                            appendNewline(2)
                        }
                        append(error)
                    }
                }
            }
        }
    }

    type {
        notice {
            if (errors.isEmpty()) {
                require(clan != null) { "Clan must not be null if there are no errors. This should never happen" }

                successNoticeButton(selfClanPlayer, clanPlayer, clan)
            } else {
                errorNoticeButton(selfClanPlayer, clanPlayer, name, tag)
            }
        }
    }
}

private fun successNoticeButton(
    selfClanPlayer: ClanPlayer,
    clanPlayer: ClanPlayer,
    clan: Clan
) = actionButton {
    label { text("Clan ansehen") }
    tooltip { info("Klicke, um den erstellten Clan anzusehen.") }
    width(200)

    action {
        playerCallback { player ->
            player.showDialog(CurrentClanDialog.createDialog(selfClanPlayer, clanPlayer, clan))
        }
    }
}

private fun errorNoticeButton(
    selfClanPlayer: ClanPlayer,
    clanPlayer: ClanPlayer,
    name: String,
    tag: String,
) = actionButton {
    label { text("Zurück") }
    tooltip { info("Klicke, um zur Eingabe zurückzukehren.") }
    width(200)

    action {
        playerCallback { player ->
            player.showDialog(CreateClanDialog.createDialog(selfClanPlayer, clanPlayer, name, tag))
        }
    }
}