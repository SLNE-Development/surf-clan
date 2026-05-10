package dev.slne.clan.paper.commands.subcommands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.stringArgument
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.clan.ClanCreationResult
import dev.slne.clan.paper.permission.ClanPermissions
import dev.slne.surf.api.core.messages.adventure.buildText
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.api.paper.command.executors.playerExecutorSuspend
import dev.slne.surf.clan.core.clan.ClanImpl
import dev.slne.surf.clan.core.client.components.Components

fun CommandAPICommand.clanCreateCommand() = subcommand("create") {
    withPermission(ClanPermissions.CLAN_CREATE_COMMAND)

    stringArgument("name")
    stringArgument("tag")

    playerExecutorSuspend { player, args ->
        val name: String by args
        val tag: String by args

        val result = Clan.createClan(name, tag, player.uniqueId)
        if (result !is ClanCreationResult.Success) {
            player.sendText {
                appendErrorPrefix()
                append(renderClanCreationResult(result))
            }
            return@playerExecutorSuspend
        }

        val clan = result.clan as ClanImpl

        player.sendText {
            appendSuccessPrefix()
            success("Der Clan ")
            append(Components.Clan.renderClanInformationHover(clan))
            success(" wurde erfolgreich erstellt.")
        }
    }
}

private fun renderClanCreationResult(result: ClanCreationResult) = buildText {
    when (result) {
        is ClanCreationResult.InvalidTagOrName -> append(Components.Clan.renderClanValidation(result.reason))
        ClanCreationResult.OwnerIsAlreadyInClan -> error("Du bist bereits in einem Clan.")
        ClanCreationResult.ClanNameAlreadyExists -> error("Ein Clan mit diesem Namen existiert bereits.")
        ClanCreationResult.ClanTagAlreadyExists -> error("Ein Clan mit diesem Tag existiert bereits.")
        ClanCreationResult.ClanAlreadyExists -> error("Ein Clan mit diesem Namen oder Tag existiert bereits.")
        else -> Unit
    }
}
