package dev.slne.surf.clan.paper.commands

import com.github.shynixn.mccoroutine.folia.launch
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerArgument
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.surf.clan.paper.dialogs.MainClanDialog
import dev.slne.surf.clan.paper.permissions.Permissions
import dev.slne.surf.clan.paper.plugin
import org.bukkit.entity.Player

fun clanCommand() = commandAPICommand("clan") {
    withPermission(Permissions.COMMAND_GENERIC)

    playerArgument("targetPlayer", optional = true)

    playerExecutor { executor, args ->
        val targetPlayer: Player? by args
        val usableTargetPlayer = targetPlayer ?: executor

        plugin.launch {
            executor.showDialog(MainClanDialog.createDialog(executor, usableTargetPlayer))
        }
    }
}