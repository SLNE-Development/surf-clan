package dev.slne.clan.velocity.commands.subcommands

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.stringArgument
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.clan.core.Messages
import dev.slne.clan.core.service.ClanPlayerService
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.core.utils.CLAN_COMPONENT_BAR_COLOR
import dev.slne.clan.core.utils.formatted
import dev.slne.clan.velocity.extensions.findClan
import dev.slne.clan.velocity.plugin
import dev.slne.surf.surfapi.core.api.messages.appendText
import dev.slne.surf.surfapi.core.api.messages.buildText
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import kotlin.jvm.optionals.getOrNull


class ClanInfoCommand(
    clanService: ClanService,
    clanPlayerService: ClanPlayerService
) : CommandAPICommand("info") {
    init {
        withPermission("surf.clan.info")
        stringArgument("clanTag", optional = true)

        playerExecutor { player, args ->
            plugin.container.launch {
                val clanTag = args.getOptionalUnchecked<String>("clanTag").getOrNull()
                val clan = if (clanTag != null) {
                    clanService.findClanByTag(clanTag)
                } else {
                    player.findClan(clanService)
                }

                if (clan == null) {
                    if (clanTag != null) {
                        player.sendMessage(Messages.unknownClanComponent(clanTag))
                    } else {
                        player.sendMessage(Messages.notInClanComponent)
                    }

                    return@launch
                }

                val createdBy =
                    clanPlayerService.findClanPlayerByUuid(clan.createdBy)?.username ?: "Unbekannt"
                val clanInfoComponent = buildText {
                    appendText("ɪɴғᴏʀᴍᴀᴛɪᴏɴᴇɴ", CLAN_COMPONENT_BAR_COLOR) {
                        decorate(TextDecoration.BOLD)
                    }
                    appendNewline()

                    append(renderLine("ɴᴀᴍᴇ", clan.name))
                    appendNewline()

                    append(renderLine("ᴛᴀɢ", clan.tag))
                    appendNewline()

                    append(renderLine("ᴇʀsᴛᴇʟʟᴛ ᴠᴏɴ", createdBy))
                    appendNewline()

                    append(renderLine("ᴍɪᴛɢʟɪᴇᴅᴇʀ", clan.members.size.toString()))
                    appendNewline()

                    append(renderLine("ᴇɪɴʟᴀᴅᴜɴɢᴇɴ", clan.invites.size.toString()))
                    appendNewline()

                    append(
                        renderLine(
                            "ᴀɴғüʜʀᴇʀ",
                            clan.members.count { it.role == ClanMemberRole.LEADER }.toString()
                        )
                    )
                    appendNewline()

                    append(
                        renderLine(
                            "ᴏғғɪᴢɪᴇʀᴇ",
                            clan.members.filter { it.role == ClanMemberRole.OFFICER }.size.toString()
                        )
                    )
                    appendNewline()

                    append(
                        renderLine(
                            "ᴇʀsᴛᴇʟʟᴛ ᴀᴍ",
                            clan.createdAt?.formatted() ?: "/"
                        )
                    )
                    appendNewline()

                    append(
                        renderLine(
                            "ᴀᴋᴛᴜᴀʟɪsɪᴇʀᴛ ᴀᴍ",
                            clan.updatedAt?.formatted() ?: "/"
                        )
                    )
                }

                player.sendMessage(clanInfoComponent)
            }
        }
    }

    private fun renderLine(key: String, value: String) =
        Component.text()
            .append(Component.text("| ", CLAN_COMPONENT_BAR_COLOR, TextDecoration.BOLD))
            .append(Component.text("$key: ", NamedTextColor.GRAY))
            .append(Component.text(value, NamedTextColor.WHITE))
}