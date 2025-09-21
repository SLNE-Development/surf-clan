package dev.slne.surf.clan.core.common.clan.actions

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.actions.ChangeDiscordInviteAction
import dev.slne.surf.clan.api.common.clan.actions.ChangeDiscordInviteArguments
import dev.slne.surf.clan.api.common.clan.member.role.permission.ClanPermission
import dev.slne.surf.clan.api.common.clan.result.ClanSetDiscordInviteResult
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.util.ComponentResult
import dev.slne.surf.clan.core.common.utils.ClanActionCommon
import org.springframework.stereotype.Component

private val discordInviteLinkPattern = Regex(
    pattern = """^(https?://)?(www\.)?(discord\.gg|discordapp\.com/invite)/[A-Za-z0-9]+/?$""",
    options = setOf(RegexOption.IGNORE_CASE)
)

@Component
class ChangeDiscordInviteActionCommon : ClanActionCommon<ChangeDiscordInviteArguments>(),
    ChangeDiscordInviteAction {
    override val permission = ClanPermission.OPTIONS_DISCORD

    override suspend fun action(
        clan: Clan,
        player: ClanPlayer,
        arguments: ChangeDiscordInviteArguments
    ): ComponentResult {
        val invite = arguments.newInvite

        if (invite != null) {
            val validLength = invite.length < 255
            val validPattern = discordInviteLinkPattern.matches(invite)

            if (!validPattern || !validLength) {
                return ClanSetDiscordInviteResult.InvalidDiscordInvite(invite)
            }
        }

        return clanManager.setDiscordInvite(clan, player, invite)
    }

}