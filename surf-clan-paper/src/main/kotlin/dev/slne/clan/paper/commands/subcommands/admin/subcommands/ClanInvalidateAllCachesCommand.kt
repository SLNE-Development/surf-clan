package dev.slne.clan.paper.commands.subcommands.admin.subcommands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.paper.permission.ClanPermissions
import dev.slne.surf.api.paper.command.executors.anyExecutorSuspend
import dev.slne.surf.clan.core.client.command.invalidatedCachesMessage
import dev.slne.surf.clan.core.client.command.invalidatingCachesMessage
import dev.slne.surf.clan.core.client.redis.RedisService

fun CommandAPICommand.clanInvalidateAllCachesCommand() = subcommand("invalidateAllCaches") {
    withPermission(ClanPermissions.CLAN_ADMIN_INVALIDATE_CACHE_COMMAND)

    anyExecutorSuspend { source, arguments ->
        source.sendMessage(invalidatingCachesMessage())

        RedisService.get().invalidateAllCaches()

        source.sendMessage(invalidatedCachesMessage())
    }
}
