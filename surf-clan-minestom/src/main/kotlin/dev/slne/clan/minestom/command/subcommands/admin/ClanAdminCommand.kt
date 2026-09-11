package dev.slne.clan.minestom.command.subcommands.admin

import dev.slne.clan.minestom.command.arguments.clanByClanTagArgument
import dev.slne.clan.minestom.command.resolveClanArgument
import dev.slne.minestom.lobby.api.command.commandapi.CommandAPICommand
import dev.slne.minestom.lobby.api.command.commandapi.dsl.anyExecutor
import dev.slne.minestom.lobby.api.command.commandapi.dsl.anyExecutorSuspend
import dev.slne.minestom.lobby.api.command.commandapi.dsl.subcommand
import dev.slne.surf.clan.core.clan.ClanImpl
import dev.slne.surf.clan.core.client.command.clanConfigReloadedMessage
import dev.slne.surf.clan.core.client.command.clanDeletedMessage
import dev.slne.surf.clan.core.client.command.invalidatedCachesMessage
import dev.slne.surf.clan.core.client.command.invalidatingCachesMessage
import dev.slne.surf.clan.core.client.permission.ClanPermissions
import dev.slne.surf.clan.core.client.redis.RedisService
import dev.slne.surf.clan.core.config.ClanConfig

fun CommandAPICommand.clanAdminCommand(): CommandAPICommand = withSubcommand(
    subcommand("admin") {
        withPermission(ClanPermissions.CLAN_ADMIN_COMMAND)

        clanReloadCommand()
        clanInvalidateAllCachesCommand()
        clanDeleteCommand()
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
