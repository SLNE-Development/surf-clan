package dev.slne.clan.minestom.command.subcommands.admin

import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.clan.minestom.command.arguments.clanByClanTagArgument
import dev.slne.clan.minestom.command.arguments.clanInviteeNameArgument
import dev.slne.clan.minestom.command.arguments.clanMemberNameArgument
import dev.slne.clan.minestom.command.arguments.clanMemberRoleArgument
import dev.slne.clan.minestom.command.failCommand
import dev.slne.clan.minestom.command.resolveClanArgument
import dev.slne.surf.api.minestom.command.CommandAPICommand
import dev.slne.surf.api.minestom.command.dsl.anyExecutor
import dev.slne.surf.api.minestom.command.dsl.anyExecutorSuspend
import dev.slne.surf.api.minestom.command.dsl.greedyStringArgument
import dev.slne.surf.api.minestom.command.dsl.includeSuggestions
import dev.slne.surf.api.minestom.command.dsl.subcommand
import dev.slne.surf.clan.core.clan.ClanImpl
import dev.slne.surf.clan.core.client.Messages
import dev.slne.surf.clan.core.client.command.*
import dev.slne.surf.clan.core.client.permission.ClanPermissions
import dev.slne.surf.clan.core.client.redis.RedisService
import dev.slne.surf.clan.core.config.ClanConfig
import dev.slne.surf.core.api.common.player.SurfPlayer
import dev.slne.surf.core.api.minestom.command.argument.surfOfflinePlayerArgument
import kotlinx.coroutines.Deferred
import net.minestom.server.command.CommandSender
import net.minestom.server.entity.Player

fun CommandAPICommand.clanAdminCommand(): CommandAPICommand = withSubcommand(
    subcommand("admin") {
        withPermission(ClanPermissions.CLAN_ADMIN_COMMAND)

        clanReloadCommand()
        clanInvalidateAllCachesCommand()
        clanDeleteCommand()

        clanAdminSetRoleCommand()
        clanAdminTransferOwnerCommand()
        clanAdminKickCommand()
        clanAdminAddCommand()
        clanAdminDescriptionCommand()
        clanAdminDiscordCommand()
        clanAdminInvitesCommand()
        clanAdminRevokeInviteCommand()
    }
)

private fun CommandAPICommand.clanReloadCommand(): CommandAPICommand = withSubcommand(
    subcommand("reload") {
        withPermission(ClanPermissions.CLAN_ADMIN_RELOAD_COMMAND)

        anyExecutor { source, _ ->
            ClanConfig.reloadFromFile()

            source.sendMessage(clanConfigReloadedMessage())
        }
    }
)

private fun CommandAPICommand.clanInvalidateAllCachesCommand(): CommandAPICommand = withSubcommand(
    subcommand("invalidateAllCaches") {
        withPermission(ClanPermissions.CLAN_ADMIN_INVALIDATE_CACHE_COMMAND)

        anyExecutorSuspend { source, _ ->
            source.sendMessage(invalidatingCachesMessage())

            RedisService.get().invalidateAllCaches()

            source.sendMessage(invalidatedCachesMessage())
        }
    }
)

private fun CommandAPICommand.clanDeleteCommand(): CommandAPICommand = withSubcommand(
    subcommand("delete") {
        withPermission(ClanPermissions.CLAN_ADMIN_DELETE_COMMAND)
        clanByClanTagArgument("clan")

        anyExecutorSuspend { sender, arguments ->
            val clan = arguments.resolveClanArgument("clan") as ClanImpl
            val deleted = clan.delete()
            sender.sendMessage(clanDeletedMessage(clan, deleted))
        }
    }
)

private fun CommandAPICommand.clanAdminSetRoleCommand(): CommandAPICommand = withSubcommand(
    subcommand("setrole") {
        withPermission(ClanPermissions.CLAN_ADMIN_SET_ROLE_COMMAND)
        clanByClanTagArgument("clan")
        clanMemberNameArgument("player", "clan")
        clanMemberRoleArgument("role")

        anyExecutorSuspend { sender, args ->
            val clan = args.resolveClanArgument("clan")
            val target = args.get<String>("player")
            val role = args.get<ClanMemberRole>("role")

            sender.sendClanAdminResult(adminSetRole(sender.clanAdminActor(), clan, target, role))
        }
    }
)

private fun CommandAPICommand.clanAdminTransferOwnerCommand(): CommandAPICommand = withSubcommand(
    subcommand("transferowner") {
        withPermission(ClanPermissions.CLAN_ADMIN_TRANSFER_OWNER_COMMAND)
        clanByClanTagArgument("clan")
        clanMemberNameArgument("player", "clan")

        anyExecutorSuspend { sender, args ->
            val clan = args.resolveClanArgument("clan")
            val target = args.get<String>("player")

            sender.sendClanAdminResult(adminTransferOwner(sender.clanAdminActor(), clan, target))
        }
    }
)

private fun CommandAPICommand.clanAdminKickCommand(): CommandAPICommand = withSubcommand(
    subcommand("kick") {
        withPermission(ClanPermissions.CLAN_ADMIN_KICK_COMMAND)
        clanByClanTagArgument("clan")
        clanMemberNameArgument("player", "clan")

        anyExecutorSuspend { sender, args ->
            val clan = args.resolveClanArgument("clan")
            val target = args.get<String>("player")

            sender.sendClanAdminResult(adminKick(sender.clanAdminActor(), clan, target))
        }
    }
)

private fun CommandAPICommand.clanAdminAddCommand(): CommandAPICommand = withSubcommand(
    subcommand("add") {
        withPermission(ClanPermissions.CLAN_ADMIN_ADD_COMMAND)
        clanByClanTagArgument("clan")
        surfOfflinePlayerArgument("player")
        clanMemberRoleArgument("role", optional = true)

        anyExecutorSuspend { sender, args ->
            val clan = args.resolveClanArgument("clan")
            val player = args.get<Deferred<SurfPlayer?>>("player").await()
                ?: failCommand(Messages.PLAYER_NOT_FOUND)
            val role = args.getOptional<ClanMemberRole>("role") ?: ClanMemberRole.MEMBER
            val playerName = player.lastKnownName ?: player.uuid.toString()

            sender.sendClanAdminResult(adminAdd(sender.clanAdminActor(), clan, player.uuid, playerName, role))
        }
    }
)

private fun CommandAPICommand.clanAdminDescriptionCommand(): CommandAPICommand = withSubcommand(
    subcommand("description") {
        withPermission(ClanPermissions.CLAN_ADMIN_DESCRIPTION_COMMAND)

        withSubcommand(subcommand("set") {
            clanByClanTagArgument("clan")
            greedyStringArgument("text")

            anyExecutorSuspend { sender, args ->
                val clan = args.resolveClanArgument("clan")
                val text = args.get<String>("text")

                sender.sendClanAdminResult(adminSetDescription(sender.clanAdminActor(), clan, text))
            }
        })

        withSubcommand(subcommand("clear") {
            clanByClanTagArgument("clan")

            anyExecutorSuspend { sender, args ->
                val clan = args.resolveClanArgument("clan")

                sender.sendClanAdminResult(adminSetDescription(sender.clanAdminActor(), clan, null))
            }
        })
    }
)

private fun CommandAPICommand.clanAdminDiscordCommand(): CommandAPICommand = withSubcommand(
    subcommand("discord") {
        withPermission(ClanPermissions.CLAN_ADMIN_DISCORD_COMMAND)

        withSubcommand(subcommand("set") {
            clanByClanTagArgument("clan")
            greedyStringArgument("link") {
                includeSuggestions(*(DISCORD_LINK_SUGGESTIONS - REMOVE_DISCORD_LINK).toTypedArray())
            }

            anyExecutorSuspend { sender, args ->
                val clan = args.resolveClanArgument("clan")
                val link = args.get<String>("link")

                sender.sendClanAdminResult(adminSetDiscord(sender.clanAdminActor(), clan, link))
            }
        })

        withSubcommand(subcommand("clear") {
            clanByClanTagArgument("clan")

            anyExecutorSuspend { sender, args ->
                val clan = args.resolveClanArgument("clan")

                sender.sendClanAdminResult(adminSetDiscord(sender.clanAdminActor(), clan, null))
            }
        })
    }
)

private fun CommandAPICommand.clanAdminInvitesCommand(): CommandAPICommand = withSubcommand(
    subcommand("invites") {
        withPermission(ClanPermissions.CLAN_ADMIN_INVITES_COMMAND)
        clanByClanTagArgument("clan")

        anyExecutorSuspend { sender, args ->
            val clan = args.resolveClanArgument("clan")

            sender.sendMessage(adminInvitesMessage(clan))
        }
    }
)

private fun CommandAPICommand.clanAdminRevokeInviteCommand(): CommandAPICommand = withSubcommand(
    subcommand("revokeinvite") {
        withPermission(ClanPermissions.CLAN_ADMIN_REVOKE_INVITE_COMMAND)
        clanByClanTagArgument("clan")
        clanInviteeNameArgument("player", "clan")

        anyExecutorSuspend { sender, args ->
            val clan = args.resolveClanArgument("clan")
            val target = args.get<String>("player")

            sender.sendClanAdminResult(adminRevokeInvite(sender.clanAdminActor(), clan, target))
        }
    }
)

/**
 * The sender as the actor of an admin action, without a uuid for the console.
 */
private fun CommandSender.clanAdminActor() = when (this) {
    is Player -> ClanAdminActor(username, uuid)
    else -> ClanAdminActor("Konsole", null)
}

/**
 * Sends the confirmation of [result], or fails the command with its reason.
 */
private fun CommandSender.sendClanAdminResult(result: ClanAdminResult) = when (result) {
    is ClanAdminResult.Success -> sendMessage(result.message)
    is ClanAdminResult.Failure -> failCommand(result.message)
}
