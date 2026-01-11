package dev.slne.clan.velocity.commands.subcommands.admin.subcommands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.core.redis.RedisService
import dev.slne.clan.velocity.permission.ClanPermissions
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surf.surfapi.velocity.api.command.executors.anyExecutorSuspend

fun CommandAPICommand.clanInvalidateAllCachesCommand() = subcommand("invalidateAllCaches") {
    withPermission(ClanPermissions.CLAN_ADMIN_INVALIDATE_CACHE_COMMAND)

    anyExecutorSuspend { source, arguments ->
        source.sendText {
            info("Invalidated all caches...")
        }

        RedisService.get().invalidateAllCaches()

        source.sendText {
            success("Caches invalidated.")
        }
    }
}