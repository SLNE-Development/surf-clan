package dev.slne.clan.velocity.commands.arguments

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import com.velocitypowered.api.command.VelocityBrigadierMessage
import com.velocitypowered.api.proxy.Player
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.CommandAPIArgumentType
import dev.jorel.commandapi.executors.CommandArguments
import dev.slne.clan.api.member.ClanMember
import dev.slne.clan.velocity.extensions.findClan
import dev.slne.surf.surfapi.core.api.messages.adventure.text

const val CLAN_MEMBER_ARGUMENT_NODE_NAME = "target"

class ClanMemberArgument(nodeName: String = CLAN_MEMBER_ARGUMENT_NODE_NAME) :
    Argument<ClanMember>(nodeName, StringArgumentType.string()) {

    init {
        replaceSuggestions(ArgumentSuggestions.stringCollection { info ->
            val player = info.sender as? Player ?: return@stringCollection emptyList()
            val clan = player.findClan() ?: return@stringCollection emptyList()

            clan.members.mapNotNull { it.player.username }
        })
    }

    override fun getPrimitiveType() = ClanMember::class.java
    override fun getArgumentType() = CommandAPIArgumentType.PRIMITIVE_STRING

    override fun <Source : Any> parseArgument(
        cmdCtx: CommandContext<Source>,
        key: String,
        previousArgs: CommandArguments
    ): ClanMember {
        val memberName = StringArgumentType.getString(cmdCtx, key)

        val player = cmdCtx.source as? Player ?: run {
            throw SimpleCommandExceptionType(
                VelocityBrigadierMessage.tooltip(text("Only players can use this command"))
            ).create()
        }

        val clan = player.findClan() ?: run {
            throw SimpleCommandExceptionType(
                VelocityBrigadierMessage.tooltip(text("You are not in a clan"))
            ).create()
        }

        return clan.members.singleOrNull { it.player.username == memberName } ?: run {
            throw SimpleCommandExceptionType(
                VelocityBrigadierMessage.tooltip(text("Clan member $memberName not found"))
            ).create()
        }
    }
}

inline fun CommandAPICommand.clanMemberArgument(
    nodeName: String = CLAN_MEMBER_ARGUMENT_NODE_NAME,
    optional: Boolean = false,
    block: Argument<*>.() -> Unit = {}
): CommandAPICommand = withArguments(
    ClanMemberArgument(nodeName).setOptional(optional).apply(block)
)
