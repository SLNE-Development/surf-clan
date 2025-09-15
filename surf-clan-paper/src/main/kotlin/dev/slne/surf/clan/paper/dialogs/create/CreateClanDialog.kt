@file:Suppress("UnstableApiUsage")

package dev.slne.surf.clan.paper.dialogs.create

import dev.slne.surf.clan.api.common.clan.CLAN_NAME_MAX_LENGTH
import dev.slne.surf.clan.api.common.clan.CLAN_NAME_MIN_LENGTH
import dev.slne.surf.clan.api.common.clan.CLAN_TAG_MIN_LENGTH
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.paper.dialogs.appendClanDialogTitle
import dev.slne.surf.clan.paper.dialogs.create.buttons.createConfirmButton
import dev.slne.surf.clan.paper.dialogs.create.buttons.createDenyButton
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import net.kyori.adventure.text.format.TextDecoration

object CreateClanDialog

fun CreateClanDialog.nameInputFieldName() = "clan_name"
fun CreateClanDialog.tagInputFieldName() = "clan_tag"

fun CreateClanDialog.createDialog(
    selfClanPlayer: ClanPlayer,
    clanPlayer: ClanPlayer,
    name: String? = null,
    tag: String? = null
) = dialog {
    base {
        title { appendClanDialogTitle(buildText { primary("Clan erstellen") }) }
        body {
            plainMessage {
                info("Hier kannst du einen neuen Clan erstellen.")
                appendNewline(2)
                info("Lege im folgenden einen Namen und Tag für deinen Clan fest.")
                appendNewline(2)

                info("Der Name deines Clans muss zwischen ")
                variableValue(CLAN_NAME_MIN_LENGTH)
                info(" und ")
                variableValue(CLAN_NAME_MAX_LENGTH)
                info(" Zeichen lang sein.")
                appendSpace()

                info("Der ")
                append {
                    variableValue("Tag", TextDecoration.UNDERLINED, TextDecoration.BOLD)

                    hoverEvent(buildText {
                        spacer("Der Clan-Tag ist das Kürzel deines Clans, das im Spiel angezeigt wird.")
                    })
                }
                info(" deines Clans muss zwischen ")
                variableValue(CLAN_TAG_MIN_LENGTH)
                info(" und ")
                variableValue(CLAN_TAG_MIN_LENGTH)
                info(" Zeichen lang sein.")
                appendNewline()

                input {
                    text(nameInputFieldName()) {
                        width(300)
                        initial(name ?: "")
                        maxLength(CLAN_NAME_MAX_LENGTH)
                    }
                    text(tagInputFieldName()) {
                        width(300)
                        initial(tag ?: "")
                        maxLength(CLAN_TAG_MIN_LENGTH)
                    }
                }

            }
        }
    }
    type {
        confirmation(
            CreateClanDialog.createDenyButton(selfClanPlayer, clanPlayer),
            CreateClanDialog.createConfirmButton(selfClanPlayer, clanPlayer)
        )
    }


}
