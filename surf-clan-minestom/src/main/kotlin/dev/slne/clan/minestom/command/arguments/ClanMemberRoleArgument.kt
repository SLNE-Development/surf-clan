package dev.slne.clan.minestom.command.arguments

import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.clan.minestom.command.failCommand
import dev.slne.surf.api.minestom.command.CommandAPICommand
import dev.slne.surf.api.minestom.command.argument.Argument
import dev.slne.surf.api.minestom.command.argument.CustomArgument
import dev.slne.surf.api.minestom.command.argument.StringArgument
import dev.slne.surf.api.minestom.command.suggestion.ArgumentSuggestions
import dev.slne.surf.clan.core.client.command.CLAN_ROLE_NAMES
import dev.slne.surf.clan.core.client.command.UNKNOWN_CLAN_ROLE
import dev.slne.surf.clan.core.client.command.parseClanRole

/**
 * A clan role, written as its lowercase name.
 */
class ClanMemberRoleArgument(nodeName: String) :
    CustomArgument<ClanMemberRole, String>(StringArgument(nodeName), { info ->
        parseClanRole(info.currentInput) ?: failCommand(UNKNOWN_CLAN_ROLE)
    }) {

    init {
        replaceSuggestions(ArgumentSuggestions.strings(CLAN_ROLE_NAMES))
    }
}

inline fun CommandAPICommand.clanMemberRoleArgument(
    nodeName: String,
    optional: Boolean = false,
    block: Argument<ClanMemberRole>.() -> Unit = {}
): CommandAPICommand =
    withArguments(ClanMemberRoleArgument(nodeName).setOptional(optional).apply(block))
