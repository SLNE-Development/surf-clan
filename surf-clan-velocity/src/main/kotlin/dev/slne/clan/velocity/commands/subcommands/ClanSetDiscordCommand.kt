package dev.slne.clan.velocity.commands.subcommands

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.kotlindsl.greedyStringArgument
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.velocity.permission.ClanPermissions
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surf.surfapi.velocity.api.command.executors.playerExecutorSuspend

private val DISCORD_LINK_REGEX =
    """^(?:https?://)?(?:www\.)?(?:discord\.gg|discord(?:app)?\.com/invite)/[A-Za-z0-9-]+/?$""".toRegex()

fun CommandAPICommand.clanSetDiscordCommand() = subcommand("setdiscord") {
    withPermission(ClanPermissions.CLAN_SET_DISCORD_COMMAND)

    greedyStringArgument("link") {
        includeSuggestions(
            ArgumentSuggestions.strings(
                "https://discord.gg/castcrafter",
                "https://discord.com/invite/castcrafter",
                "NULL"
            )
        )
    }

    playerExecutorSuspend { player, args ->
        val rawLink = args.getUnchecked<String>("link")!!
        val clan = Clan.byPlayer(player.uniqueId) ?: throw CommandAPI.failWithString("Du bist in keinem Clan.")

        if (!clan.hasMemberPermission(player.uniqueId, ClanPermission.DISCORD)) {
            throw CommandAPI.failWithString("Du hast keine Berechtigung, den Discord Link zu ändern.")
        }

        if (clan.members.size < Clan.DISCORD_LINK_REQUIRED_MEMBERS) {
            throw CommandAPI.failWithString("Dein Clan muss mindestens ${Clan.DISCORD_LINK_REQUIRED_MEMBERS} Mitglieder haben, um den Discord-Link ändern zu können.")
        }

        val isNullLink = rawLink == "NULL"

        if (isNullLink) {
            clan.setDiscordInvite(null)
        } else {
            if (!rawLink.matches(DISCORD_LINK_REGEX)) {
                throw CommandAPI.failWithString("Du musst einen gültigen Discord-Invite Link angeben!")
            }

            clan.setDiscordInvite(rawLink)
        }

        player.sendText {
            appendPrefix()
            success("Der Discord Link wurde erfolgreich ")
            if (isNullLink) {
                success("entfernt.")
            } else {
                success("auf ")
                variableValue(rawLink)
                success(" gesetzt.")
            }
        }
    }
}