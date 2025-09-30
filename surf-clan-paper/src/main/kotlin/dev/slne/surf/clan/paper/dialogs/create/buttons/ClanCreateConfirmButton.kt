@file:Suppress("UnstableApiUsage")

package dev.slne.surf.clan.paper.dialogs.create.buttons

import dev.slne.surf.clan.api.common.clan.tag.ClanTag
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.paper.dialogs.create.CreateClanDialog
import dev.slne.surf.clan.paper.dialogs.create.results.createClanCreationNoticeDialog
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import io.papermc.paper.registry.data.dialog.ActionButton
import net.kyori.adventure.text.ComponentLike

fun CreateClanDialog.createConfirmButton(
    selfClanPlayer: ClanPlayer,
    clanPlayer: ClanPlayer
): ActionButton = actionButton {
    label { success("Clan erstellen") }
    tooltip { info("Klicke, um den Clan zu erstellen.") }
    width(200)

    action {
        customPlayerClick { info, player ->
            val nameInput = info.getText(CreateClanDialog.NAME_INPUT_FIELD_NAME)
                ?.trim()
                ?.replace(" ", "")
                ?: ""

            val tagInput = info.getText(CreateClanDialog.TAG_INPUT_FIELD_NAME)
                ?.trim()
                ?.replace(" ", "")
                ?: ""

            val nameResult = ClanTag.Validator.validateName(nameInput)
            val tagResult = ClanTag.Validator.validateTag(tagInput)

            val errors = mutableListOf<ComponentLike>()
            errors.addIf(!nameResult.isSuccess, nameResult)
            errors.addIf(!tagResult.isSuccess, tagResult)

            // CREATE CLAN IN DB

            player.showDialog(
                CreateClanDialog.createClanCreationNoticeDialog(
                    selfClanPlayer,
                    clanPlayer,
                    clan,
                    nameInput,
                    tagInput,
                    errors
                )
            )
        }
    }
}

private fun <T> MutableList<T>.addIf(condition: Boolean, element: T) {
    if (condition) {
        this.add(element)
    }
}