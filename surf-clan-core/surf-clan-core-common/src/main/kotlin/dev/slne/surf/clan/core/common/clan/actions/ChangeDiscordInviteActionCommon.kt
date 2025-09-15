package dev.slne.surf.clan.core.common.clan.actions

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.actions.ChangeDiscordInviteAction
import dev.slne.surf.clan.api.common.clan.member.role.permission.ClanPermission
import dev.slne.surf.clan.api.common.clan.result.ClanSetDiscordInviteResult
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.util.ClanActionExtraArguments
import dev.slne.surf.clan.api.common.util.ComponentResult

private val discordInviteLinkPattern = Regex(
    pattern = """^(https?://)?(www\.)?(discord\.gg|discordapp\.com/invite)/[A-Za-z0-9]+/?$""",
    options = setOf(RegexOption.IGNORE_CASE)
)

object ChangeDiscordInviteActionCommon : ChangeDiscordInviteAction {
    override val permission = ClanPermission.OPTIONS_DISCORD

    override suspend fun authorizeExtra(
        clan: Clan,
        player: ClanPlayer,
        arguments: ClanActionExtraArguments
    ): ComponentResult? {
        val invite: String? by arguments

        if (invite != null && (invite.length > 255 || !discordInviteLinkPattern.matches(
                invite
            ))
        ) {
            return ClanSetDiscordInviteResult.InvalidDiscordInvite(invite)
        }
    }
}