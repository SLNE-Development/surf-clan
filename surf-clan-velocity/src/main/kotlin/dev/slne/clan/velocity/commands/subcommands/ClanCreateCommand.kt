package dev.slne.clan.velocity.commands.subcommands

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.stringArgument
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.clan.core.*
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.core.utils.clanComponent
import dev.slne.clan.velocity.extensions.findClan
import dev.slne.clan.velocity.plugin
import net.kyori.adventure.text.Component

class ClanCreateCommand(clanService: ClanService) : CommandAPICommand("create") {
    init {
        withPermission("surf.clan.create")

        stringArgument("name")
        stringArgument("tag")

        executesPlayer(PlayerCommandExecutor { player, args ->
            val name: String by args
            val tag: String by args

            val findClan = player.findClan(clanService)

            if (findClan != null) {
                player.sendMessage(buildMessage {
                    append(Component.text("Du bist bereits im Clan ", COLOR_ERROR))
                    append(clanComponent(findClan))
                    append(Component.text(".", COLOR_ERROR))
                })

                return@PlayerCommandExecutor
            }

            val findClanByName = clanService.findClanByName(name)
            if (findClanByName != null) {
                player.sendMessage(buildMessage {
                    append(Component.text("Ein Clan mit dem Namen ", COLOR_ERROR))
                    append(clanComponent(findClanByName))
                    append(Component.text(" existiert bereits.", COLOR_ERROR))
                })

                return@PlayerCommandExecutor
            }

            val findClanByTag = clanService.findClanByTag(tag)
            if (findClanByTag != null) {
                player.sendMessage(buildMessage {
                    append(Component.text("Ein Clan mit dem Tag ", COLOR_ERROR))
                    append(clanComponent(findClanByTag))
                    append(Component.text(" existiert bereits.", COLOR_ERROR))
                })

                return@PlayerCommandExecutor
            }

            if (tag.length < 3 || tag.length > 4) {
                val builder = Component.text()
                builder.append(Component.text("Der Tag muss zwischen ", COLOR_ERROR))
                builder.append(Component.text("3", COLOR_VARIABLE))
                builder.append(Component.text(" und ", COLOR_ERROR))
                builder.append(Component.text("4", COLOR_VARIABLE))
                builder.append(Component.text(" Zeichen lang sein.", COLOR_ERROR))
                val message = builder.build()

                player.sendMessage(buildMessage {
                    append(message)
                })

                return@PlayerCommandExecutor
            }

            if (name.length < 3 || name.length > 16) {
                val builder = Component.text()
                builder.append(Component.text("Der Name muss zwischen ", COLOR_ERROR))
                builder.append(Component.text("3", COLOR_VARIABLE))
                builder.append(Component.text(" und ", COLOR_ERROR))
                builder.append(Component.text("16", COLOR_VARIABLE))
                builder.append(Component.text(" Zeichen lang sein.", COLOR_ERROR))
                val message = builder.build()

                player.sendMessage(buildMessage {
                    append(message)
                })

                return@PlayerCommandExecutor
            }

            plugin.container.launch {
                val uuid = clanService.createUnusedClanUuid()

                val clan = CoreClan(
                    uuid = uuid,
                    name = name,
                    tag = tag,
                    createdBy = player.uniqueId,
                )

                clan.addMember(player.uniqueId, ClanMemberRole.LEADER, player.uniqueId)

                clanService.saveClan(clan)

                player.sendMessage(buildMessage {
                    append(Component.text("Der Clan ", COLOR_SUCCESS))
                    append(clanComponent(clan))
                    append(Component.text(" wurde erfolgreich erstellt.", COLOR_SUCCESS))
                })
            }
        })
    }
}