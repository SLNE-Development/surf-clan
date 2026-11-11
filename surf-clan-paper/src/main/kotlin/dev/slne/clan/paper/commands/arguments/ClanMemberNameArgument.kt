package dev.slne.clan.paper.commands.arguments

import dev.jorel.commandapi.arguments.StringArgument
import dev.slne.surf.clan.core.client.command.memberNamesOfTag

/**
 * The name or uuid of a member of the clan named by the [clanNode] argument, suggested from that clan.
 *
 * Unlike [ClanMemberArgument], this does not resolve the member: the admin commands resolve it within
 * the chosen clan.
 */
class ClanMemberNameArgument(nodeName: String, clanNode: String) : StringArgument(nodeName) {
    init {
        replaceSuggestions(clanScopedSuggestions(clanNode) { tag -> memberNamesOfTag(tag) })
    }
}
