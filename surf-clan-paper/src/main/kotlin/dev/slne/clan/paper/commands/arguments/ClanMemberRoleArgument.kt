package dev.slne.clan.paper.commands.arguments

import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.StringArgument
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.clan.paper.commands.failCommand
import dev.slne.surf.api.paper.command.args.SuspendCustomArgument
import dev.slne.surf.clan.core.client.command.CLAN_ROLE_NAMES
import dev.slne.surf.clan.core.client.command.UNKNOWN_CLAN_ROLE
import dev.slne.surf.clan.core.client.command.parseClanRole
import kotlinx.coroutines.CoroutineScope

/**
 * A clan role, written as its lowercase name.
 */
class ClanMemberRoleArgument(nodeName: String) :
    SuspendCustomArgument<ClanMemberRole, String>(StringArgument(nodeName)) {

    init {
        replaceSuggestions(ArgumentSuggestions.strings(CLAN_ROLE_NAMES))
    }

    override suspend fun CoroutineScope.parse(info: CustomArgumentInfo<String>): ClanMemberRole =
        parseClanRole(info.currentInput) ?: failCommand(UNKNOWN_CLAN_ROLE)
}
