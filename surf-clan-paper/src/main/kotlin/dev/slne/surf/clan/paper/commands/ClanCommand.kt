package dev.slne.surf.clan.paper.commands

import com.github.shynixn.mccoroutine.folia.launch
import dev.jorel.commandapi.arguments.PlayerProfileArgument
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.surf.clan.paper.dialogs.MainClanDialog
import dev.slne.surf.clan.paper.permissions.Permissions
import dev.slne.surf.clan.paper.plugin
import dev.slne.surf.surfapi.bukkit.api.extensions.server

fun clanCommand() = commandAPICommand("clan") {
    withPermission(Permissions.COMMAND_GENERIC)

    withArguments(PlayerProfileArgument("targetPlayer").setOptional(true))

    playerExecutor { executor, arguments ->
        val targetProfile = arguments.getOrDefaultUnchecked("targetPlayer", executor.playerProfile)
        val targetOffline = server.getOfflinePlayer(targetProfile.id!!)

        plugin.launch {
            executor.showDialog(MainClanDialog.createDialog(executor, targetOffline))
        }
    }
}