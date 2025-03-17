package dev.slne.clan.velocity.commands.arguments

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import com.velocitypowered.api.command.VelocityBrigadierMessage
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.CommandAPIArgumentType
import dev.jorel.commandapi.executors.CommandArguments
import dev.slne.clan.api.Clan
import dev.slne.clan.core.service.ClanService
import dev.slne.surf.surfapi.core.api.messages.adventure.text
import it.unimi.dsi.fastutil.objects.ObjectSet

const val CLAN_ARGUMENT_NODE_NAME = "clan"

class ClanArgument(
    nodeName: String = CLAN_ARGUMENT_NODE_NAME,
    clans: ObjectSet<Clan> = ClanService.clans
) : Argument<Clan>(nodeName, StringArgumentType.string()) {

    init {
        replaceSuggestions(ArgumentSuggestions.stringCollection { _ ->
            clans.map { it.name }
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

        return ClanService.findClanByName(clanName) ?: throw SimpleCommandExceptionType(
            VelocityBrigadierMessage.tooltip(text("Clan $clanName not found"))
        ).create()
    }
}

inline fun CommandAPICommand.clanArgument(
    nodeName: String = CLAN_ARGUMENT_NODE_NAME,
    optional: Boolean = false,
    block: Argument<*>.() -> Unit = {}
): CommandAPICommand = withArguments(
    ClanArgument(nodeName).setOptional(optional).apply(block)
)
