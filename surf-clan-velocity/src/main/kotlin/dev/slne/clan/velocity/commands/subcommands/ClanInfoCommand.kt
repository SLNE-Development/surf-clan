package dev.slne.clan.velocity.commands.subcommands

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.clan.core.Messages
import dev.slne.clan.core.buildMessage
import dev.slne.clan.core.service.ClanPlayerService
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.core.utils.CLAN_COMPONENT_BAR_COLOR
import dev.slne.clan.core.utils.formatted
import dev.slne.clan.velocity.extensions.findClan
import dev.slne.clan.velocity.plugin
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration


class ClanInfoCommand(
    clanService: ClanService,
    clanPlayerService: ClanPlayerService
) : CommandAPICommand("info") {
    init {
        withPermission("surf.clan.info")

        executesPlayer(PlayerCommandExecutor { player, args ->
            plugin.container.launch {
                val clan = player.findClan(clanService)

                if (clan == null) {
                    player.sendMessage(Messages.notInClanComponent)

                    return@launch
                }

                val createdBy =
                    clanPlayerService.findClanPlayerByUuid(clan.createdBy)?.username ?: "Unbekannt"
                val clanInfoComponent = buildMessage(false) {
                    append(
                        Component.text(
                            "ɪɴғᴏʀᴍᴀᴛɪᴏɴᴇɴ",
                            CLAN_COMPONENT_BAR_COLOR,
                            TextDecoration.BOLD
                        )
                    )
                    appendNewline()

                    append(renderLine("ɴᴀᴍᴇ", clan.name))
                    appendNewline()

                    append(renderLine("ᴛᴀɢ", clan.tag))
                    appendNewline()

                    append(renderLine("ᴇʀsᴛᴇʟʟᴛ ᴠᴏɴ", createdBy))
                    appendNewline()

                    append(
                        renderLine(
                            "ᴍɪᴛɢʟɪᴇᴅᴇʀ",
                            clan.members.size.toString()
                        )
                    )
                    appendNewline()

                    append(
                        renderLine(
                            "ᴇɪɴʟᴀᴅᴜɴɢᴇɴ",
                            clan.invites.size.toString()
                        )
                    )
                    appendNewline()

                    append(
                        renderLine(
                            "ᴀɴғüʜʀᴇʀ",
                            clan.members.filter { it.role == ClanMemberRole.LEADER }.size.toString()
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
        })
    }

    private fun renderLine(key: String, value: String) =
        Component.text()
            .append(Component.text("| ", CLAN_COMPONENT_BAR_COLOR, TextDecoration.BOLD))
            .append(Component.text("$key: ", NamedTextColor.GRAY))
            .append(Component.text(value, NamedTextColor.WHITE))
}