package dev.slne.clan.velocity.commands.subcommands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import dev.slne.clan.core.*
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.core.utils.formatted
import dev.slne.clan.velocity.extensions.findClan
import net.kyori.adventure.text.Component


class ClanInfoCommand(clanService: ClanService) : CommandAPICommand("info") {
    init {
        withPermission("surf.clan.info")

        executesPlayer(PlayerCommandExecutor { player, args ->
            val clan = player.findClan(clanService)

            if (clan == null) {
                player.sendMessage(Messages.notInClanComponent)

                return@PlayerCommandExecutor
            }

            val clanInfoMessage = buildMessage(false) {
                append(Component.text("Name: ", COLOR_INFO))
                append(Component.text(clan.name, COLOR_VARIABLE))
                appendNewline()

                append(Component.text("Tag: ", COLOR_INFO))
                append(Component.text(clan.tag, COLOR_VARIABLE))
                appendNewline()

                append(Component.text("Erstellt von: ", COLOR_INFO))
                append(
                    Component.text(
                        clan.createdBy.toString(),
                        COLOR_VARIABLE
                    )
                )
                appendNewline()

                append(Component.text("Mitglieder: ", COLOR_INFO))
                append(Component.text(clan.members.size.toString(), COLOR_VARIABLE))
                appendNewline()

                append(Component.text("Einladungen: ", COLOR_INFO))
                append(Component.text(clan.invites.size.toString(), COLOR_VARIABLE))
                appendNewline()

                append(Component.text("Erstellt am: ", COLOR_INFO))
                append(Component.text(clan.createdAt?.formatted() ?: "/", COLOR_VARIABLE))
                appendNewline()

                append(Component.text("Aktualisiert am: ", COLOR_INFO))
                append(Component.text(clan.updatedAt?.formatted() ?: "/", COLOR_VARIABLE))

            }

            player.sendMessage(clanInfoMessage)

            return@PlayerCommandExecutor
            })
    }
}