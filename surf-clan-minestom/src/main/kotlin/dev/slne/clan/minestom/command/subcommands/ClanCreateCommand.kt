package dev.slne.clan.minestom.command.subcommands

import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.clan.ClanCreationResult
import dev.slne.minestom.lobby.api.command.commandapi.CommandAPICommand
import dev.slne.minestom.lobby.api.command.commandapi.dsl.playerExecutorSuspend
import dev.slne.minestom.lobby.api.command.commandapi.dsl.stringArgument
import dev.slne.minestom.lobby.api.command.commandapi.dsl.subcommand
import dev.slne.surf.clan.core.clan.ClanImpl
import dev.slne.surf.clan.core.client.command.clanCreatedMessage
import dev.slne.surf.clan.core.client.command.clanCreationFailedMessage
import dev.slne.surf.clan.core.client.permission.ClanPermissions

fun CommandAPICommand.clanCreateCommand(): CommandAPICommand = withSubcommand(
    subcommand("create") {
        withPermission(ClanPermissions.CLAN_CREATE_COMMAND)

        stringArgument("name")
        stringArgument("tag")

        playerExecutorSuspend { player, args ->
            val name: String by args
            val tag: String by args

            val result = Clan.createClan(name, tag, player.uuid)
            if (result !is ClanCreationResult.Success) {
                player.sendMessage(clanCreationFailedMessage(result))
                return@playerExecutorSuspend
            }

            player.sendMessage(clanCreatedMessage(result.clan as ClanImpl))
        }
    }
)
