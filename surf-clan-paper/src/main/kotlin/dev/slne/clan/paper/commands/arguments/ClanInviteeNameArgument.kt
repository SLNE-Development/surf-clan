package dev.slne.clan.paper.commands.arguments

import dev.jorel.commandapi.arguments.StringArgument
import dev.slne.surf.clan.core.client.command.pendingInviteeNamesOfTag

/**
 * The name or uuid of a player with a pending invitation into the clan named by the [clanNode]
 * argument, suggested from that clan's invitations.
 */
class ClanInviteeNameArgument(nodeName: String, clanNode: String) : StringArgument(nodeName) {
    init {
        replaceSuggestions(clanScopedSuggestions(clanNode) { tag -> pendingInviteeNamesOfTag(tag) })
    }
}
