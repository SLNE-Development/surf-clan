@file:Suppress("UnstableApiUsage")

package dev.slne.surf.clan.paper.dialogs.create.buttons

import dev.slne.surf.clan.api.common.clan.tag.ClanTag
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.core.common.clan.ClanCommon
import dev.slne.surf.clan.paper.dialogs.create.CreateClanDialog
import dev.slne.surf.clan.paper.dialogs.create.results.createClanCreationNoticeDialog
import dev.slne.surf.cloud.api.common.util.objectSetOf
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import io.papermc.paper.registry.data.dialog.ActionButton
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.format.NamedTextColor
import java.time.ZonedDateTime
import java.util.*

fun CreateClanDialog.createConfirmButton(
    selfPlayer: ClanPlayer,
    executorPlayer: ClanPlayer
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

            val nameResult =
                ClanTag.Validator.validateName(nameInput) // FIXME: 30.09.2025 13:26 this doesnt make sense
            val tagResult = ClanTag.Validator.validateTag(tagInput)

            val errors = mutableListOf<ComponentLike>()
            errors.addIf(!nameResult.isSuccess, nameResult)
            errors.addIf(!tagResult.isSuccess, tagResult)

            // FIXME: 30.09.2025 13:25 Actual impl
            val clan = ClanCommon(
                uuid = UUID.randomUUID(),
                name = nameInput,
                fullTag = ClanTag(
                    tag = tagInput,
                    foregroundColor = NamedTextColor.WHITE,
                    shadowColor = NamedTextColor.GRAY,
                    backgroundColor = NamedTextColor.BLACK
                ),
                createdByUuid = executorPlayer.uuid,
                discordInvite = null,
                members = objectSetOf(),
                invites = objectSetOf(),
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now(),
            )

            player.showDialog(
                CreateClanDialog.createClanCreationNoticeDialog(
                    selfPlayer = selfPlayer,
                    executorPlayer = executorPlayer,
                    clan = clan,
                    name = nameInput,
                    tag = tagInput,
                    errors = errors
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