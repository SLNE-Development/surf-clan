package dev.slne.clan.paper.commands.subcommands.admin.subcommands

import dev.slne.clan.paper.commands.failCommand
import dev.slne.surf.clan.core.client.command.ClanAdminActor
import dev.slne.surf.clan.core.client.command.ClanAdminResult
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * The sender as the actor of an admin action, without a uuid for the console.
 */
internal fun CommandSender.clanAdminActor() = ClanAdminActor(name, (this as? Player)?.uniqueId)

/**
 * Sends the confirmation of [result], or fails the command with its reason.
 */
internal fun CommandSender.sendClanAdminResult(result: ClanAdminResult) = when (result) {
    is ClanAdminResult.Success -> sendMessage(result.message)
    is ClanAdminResult.Failure -> failCommand(result.message)
}
