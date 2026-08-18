package dev.slne.clan.paper.commands.subcommands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.stringArgument
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.clan.ClanCreationResult
import dev.slne.clan.paper.permission.ClanPermissions
import dev.slne.surf.api.paper.command.executors.playerExecutorSuspend
import dev.slne.surf.clan.core.clan.ClanImpl
import dev.slne.surf.clan.core.client.command.clanCreatedMessage
import dev.slne.surf.clan.core.client.command.clanCreationFailedMessage

fun CommandAPICommand.clanCreateCommand() = subcommand("create") {
    withPermission(ClanPermissions.CLAN_CREATE_COMMAND)

    stringArgument("name")
    stringArgument("tag")

    playerExecutorSuspend { player, args ->
        val name: String by args
        val tag: String by args

        val result = Clan.createClan(name, tag, player.uniqueId)
        if (result !is ClanCreationResult.Success) {
            player.sendMessage(clanCreationFailedMessage(result))
            return@playerExecutorSuspend
        }

        player.sendMessage(clanCreatedMessage(result.clan as ClanImpl))
    }
}
