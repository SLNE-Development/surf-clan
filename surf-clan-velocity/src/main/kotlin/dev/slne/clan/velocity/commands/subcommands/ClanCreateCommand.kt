package dev.slne.clan.velocity.commands.subcommands

import com.github.shynixn.mccoroutine.velocity.launch
import com.velocitypowered.api.proxy.Player
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.stringArgument
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.clan.core.CoreClan
import dev.slne.clan.core.service.ClanPlayerService
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.core.utils.clanComponent
import dev.slne.clan.core.utils.isInvalidClanTag
import dev.slne.clan.velocity.extensions.findClan
import dev.slne.clan.velocity.plugin
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

object ClanCreateCommand : CommandAPICommand("create") {
    init {
        withPermission("surf.clan.create")

        stringArgument("name")
        stringArgument("tag")

        playerExecutor { player, args ->
            val name: String by args
            val tag: String by args

            if (!preValidateInput(player, tag)) {
                return@playerExecutor
            }

            plugin.container.launch {
                val findClan = player.findClan()

                if (findClan != null) {
                    player.sendText {
                        error("Du bist bereits im Clan ")
                        append(clanComponent(findClan))
                        error(".")
                    }

                    return@launch
                }

                val findClanByName = ClanService.findClanByName(name)
                if (findClanByName != null) {
                    player.sendText {
                        error("Ein Clan mit dem Namen ")
                        append(clanComponent(findClanByName))
                        error(" existiert bereits.")
                    }

                    return@launch
                }

                val findClanByTag = ClanService.findClanByTag(tag)
                if (findClanByTag != null) {
                    player.sendText {
                        error("Ein Clan mit dem Tag ")
                        append(clanComponent(findClanByTag))
                        error(" existiert bereits.")
                    }

                    return@launch
                }

                val clanPlayer = ClanPlayerService.findClanPlayer(player.uniqueId)

                val clan = CoreClan(
                    uuid = ClanService.createUnusedClanUuid(),
                    name = name,
                    tag = tag,
                    createdBy = clanPlayer,
                )

                clan.addMember(clanPlayer, ClanMemberRole.OWNER, clanPlayer)
                ClanService.saveClan(clan)

                player.sendText {
                    success("Der Clan ")
                    append(clanComponent(clan))
                    success(" wurde erfolgreich erstellt.")
                }
            }
        }
    }


    private fun preValidateInput(player: Player, tag: String): Boolean {
        if (name.length < 3 || name.length > 16) {
            player.sendText {
                error("Der Name muss zwischen ")
                variableValue("3")
                error(" und ")
                variableValue("16")
                error(" Zeichen lang sein.")
            }

            return false
        }

        if (tag.length < 3 || tag.length > 4) {
            player.sendText {
                error("Der Tag muss zwischen ")
                variableValue("3")
                error(" und ")
                variableValue("4")
                error(" Zeichen lang sein.")
            }

            return false
        }

        if (tag.any { !it.isLetterOrDigit() }) {
            player.sendText {
                error("Der Clan-Tag ")
                variableValue(tag)
                error(" ist nicht erlaubt. Er darf nur aus Buchstaben und Zahlen bestehen.")
            }

            return false
        }

        if (isInvalidClanTag(tag)) {
            player.sendText {
                error("Der Clan-Tag ")
                variableValue(tag)
                error(" ist nicht erlaubt.")
            }

            return false
        }

        return true
    }
}