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
import dev.slne.clan.api.Clan
import dev.slne.clan.core.invite.CoreClanInvite
import dev.slne.clan.velocity.extensions.findClanInvites
import dev.slne.surf.surfapi.core.api.messages.adventure.text

const val CLAN_INVITE_ARGUMENT_NODE_NAME = "clanInvite"

class ClanInviteArgument(nodeName: String = CLAN_INVITE_ARGUMENT_NODE_NAME) :
    Argument<Clan>(nodeName, StringArgumentType.string()) {

    init {
        replaceSuggestions(ArgumentSuggestions.stringCollection { info ->
            val player = info.sender as? Player ?: return@stringCollection emptyList()

            player.findClanInvites<CoreClanInvite>().map { it.clan.name }
        })
    }

    override fun getPrimitiveType() = Clan::class.java
    override fun getArgumentType() = CommandAPIArgumentType.PRIMITIVE_STRING

    override fun <Source : Any> parseArgument(
        cmdCtx: CommandContext<Source>,
        key: String,
        previousArgs: CommandArguments
    ): Clan {
        val clanName = StringArgumentType.getString(cmdCtx, key)
        val player = cmdCtx.source as? Player ?: run {
            throw SimpleCommandExceptionType(
                VelocityBrigadierMessage.tooltip(text("Only players can use this command"))
            ).create()
        }

        return player.findClanInvites<CoreClanInvite>().find { it.clan.name == clanName }?.clan
            ?: throw SimpleCommandExceptionType(
                VelocityBrigadierMessage.tooltip(text("Clan invite $clanName not found"))
            ).create()
    }
}

inline fun CommandAPICommand.clanInviteArgument(
    nodeName: String = CLAN_INVITE_ARGUMENT_NODE_NAME,
    optional: Boolean = false,
    block: Argument<*>.() -> Unit = {}
): CommandAPICommand = withArguments(
    ClanInviteArgument(nodeName).setOptional(optional).apply(block)
)
