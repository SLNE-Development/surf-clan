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
                        append(Component.text("Du bist bereits im Clan ", COLOR_ERROR))
                        append(clanComponent(findClan, clanPlayerService))
                        append(Component.text(".", COLOR_ERROR))
                    })

                    return@launch
                }

                val findClanByName = clanService.findClanByName(name)
                if (findClanByName != null) {
                    player.sendMessage(buildMessageAsync {
                        append(Component.text("Ein Clan mit dem Namen ", COLOR_ERROR))
                        append(clanComponent(findClanByName, clanPlayerService))
                        append(Component.text(" existiert bereits.", COLOR_ERROR))
                    })

                    return@launch
                }

                val findClanByTag = clanService.findClanByTag(tag)
                if (findClanByTag != null) {
                    player.sendMessage(buildMessageAsync {
                        append(Component.text("Ein Clan mit dem Tag ", COLOR_ERROR))
                        append(clanComponent(findClanByTag, clanPlayerService))
                        append(Component.text(" existiert bereits.", COLOR_ERROR))
                    })

                    return@launch
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

                    return@launch
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

                    return@launch
                }

                if (isInvalidClanTag(tag)) {
                    player.sendMessage(buildMessageAsync {
                        append(Component.text("Der Clan-Tag ", COLOR_ERROR))
                        append(Component.text(tag, COLOR_VARIABLE))
                        append(Component.text(" ist nicht erlaubt.", COLOR_ERROR))
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
                    append(Component.text("Der Clan ", COLOR_SUCCESS))
                    append(clanComponent(clan, clanPlayerService))
                    append(Component.text(" wurde erfolgreich erstellt.", COLOR_SUCCESS))
                })
            }
        })
    }
}