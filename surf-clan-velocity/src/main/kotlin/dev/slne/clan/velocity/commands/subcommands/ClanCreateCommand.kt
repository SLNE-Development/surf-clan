package dev.slne.clan.velocity.commands.subcommands

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.stringArgument
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.clan.core.*
import dev.slne.clan.core.service.ClanPlayerService
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.core.utils.clanComponent
import dev.slne.clan.core.utils.isInvalidClanTag
import dev.slne.clan.velocity.extensions.findClan
import dev.slne.clan.velocity.plugin
import dev.slne.surf.surfapi.core.api.messages.Colors
import net.kyori.adventure.text.Component

class ClanCreateCommand(
    clanService: ClanService,
    clanPlayerService: ClanPlayerService
) : CommandAPICommand("create") {
    init {
        withPermission("surf.clan.create")

        stringArgument("name")
        stringArgument("tag")

        executesPlayer(PlayerCommandExecutor { player, args ->
            plugin.container.launch {
                val name: String by args
                val tag: String by args

                val findClan = player.findClan(clanService)

                if (findClan != null) {
                    player.sendMessage(buildMessageAsync {
                        append(Component.text("Du bist bereits im Clan ", Colors.ERROR))
                        append(clanComponent(findClan, clanPlayerService))
                        append(Component.text(".", Colors.ERROR))
                    })

                    return@launch
                }

                val findClanByName = clanService.findClanByName(name)
                if (findClanByName != null) {
                    player.sendMessage(buildMessageAsync {
                        append(Component.text("Ein Clan mit dem Namen ", Colors.ERROR))
                        append(clanComponent(findClanByName, clanPlayerService))
                        append(Component.text(" existiert bereits.", Colors.ERROR))
                    })

                    return@launch
                }

                val findClanByTag = clanService.findClanByTag(tag)
                if (findClanByTag != null) {
                    player.sendMessage(buildMessageAsync {
                        append(Component.text("Ein Clan mit dem Tag ", Colors.ERROR))
                        append(clanComponent(findClanByTag, clanPlayerService))
                        append(Component.text(" existiert bereits.", Colors.ERROR))
                    })

                    return@launch
                }

                if (tag.length < 3 || tag.length > 4) {
                    val builder = Component.text()
                    builder.append(Component.text("Der Tag muss zwischen ", Colors.ERROR))
                    builder.append(Component.text("3", Colors.VARIABLE_VALUE))
                    builder.append(Component.text(" und ", Colors.ERROR))
                    builder.append(Component.text("4", Colors.VARIABLE_VALUE))
                    builder.append(Component.text(" Zeichen lang sein.", Colors.ERROR))
                    val message = builder.build()

                    player.sendMessage(buildMessage {
                        append(message)
                    })

                    return@launch
                }

                if (name.length < 3 || name.length > 16) {
                    val builder = Component.text()
                    builder.append(Component.text("Der Name muss zwischen ", Colors.ERROR))
                    builder.append(Component.text("3", Colors.VARIABLE_VALUE))
                    builder.append(Component.text(" und ", Colors.ERROR))
                    builder.append(Component.text("16", Colors.VARIABLE_VALUE))
                    builder.append(Component.text(" Zeichen lang sein.", Colors.ERROR))
                    val message = builder.build()

                    player.sendMessage(buildMessage {
                        append(message)
                    })

                    return@launch
                }

                if (isInvalidClanTag(tag)) {
                    player.sendMessage(buildMessageAsync {
                        append(Component.text("Der Clan-Tag ", Colors.ERROR))
                        append(Component.text(tag, Colors.VARIABLE_VALUE))
                        append(Component.text(" ist nicht erlaubt.", Colors.ERROR))
                    })

                    return@launch
                }

                val uuid = clanService.createUnusedClanUuid()

                val clan = CoreClan(
                    uuid = uuid,
                    name = name,
                    tag = tag,
                    createdBy = player.uniqueId,
                )

                clan.addMember(player.uniqueId, ClanMemberRole.LEADER, player.uniqueId)

                clanService.saveClan(clan)

                player.sendMessage(buildMessageAsync {
                    append(Component.text("Der Clan ", Colors.SUCCESS))
                    append(clanComponent(clan, clanPlayerService))
                    append(Component.text(" wurde erfolgreich erstellt.", Colors.SUCCESS))
                })
            }
        })
    }
}