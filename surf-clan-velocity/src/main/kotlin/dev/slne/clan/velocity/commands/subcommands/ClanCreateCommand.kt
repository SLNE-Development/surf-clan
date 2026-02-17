package dev.slne.clan.velocity.commands.subcommands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.stringArgument
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.clan.ClanCreationResult
import dev.slne.clan.api.clan.ClanValidationResult
import dev.slne.clan.core.clan.ClanImpl
import dev.slne.clan.core.components.Components
import dev.slne.clan.velocity.permission.ClanPermissions
import dev.slne.surf.surfapi.core.api.messages.Colors
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surf.surfapi.velocity.api.command.executors.playerExecutorSuspend
import net.kyori.adventure.text.format.TextDecoration

fun CommandAPICommand.clanCreateCommand() = subcommand("create") {
    withPermission(ClanPermissions.CLAN_CREATE_COMMAND)

    stringArgument("name")
    stringArgument("tag")

    playerExecutorSuspend { player, args ->
        val name: String by args
        val tag: String by args

        val result = Clan.createClan(name, tag, player.uniqueId)
        if (result !is ClanCreationResult.Success) {
            player.sendText {
                appendPrefix()
                append(renderClanCreationResult(result))
            }
            return@playerExecutorSuspend
        }

        val clan = result.clan as ClanImpl

        player.sendText {
            appendPrefix()
            success("Der Clan ")
            append(Components.Clan.renderClanInformationHover(clan))
            success(" wurde erfolgreich erstellt.")
        }
    }
}

private fun renderClanCreationResult(result: ClanCreationResult) = buildText {
    when (result) {
        is ClanCreationResult.InvalidTagOrName -> append(renderClanValidation(result.reason))
        ClanCreationResult.OwnerIsAlreadyInClan -> error("Du bist bereits in einem Clan.")
        ClanCreationResult.ClanNameAlreadyExists -> error("Ein Clan mit diesem Namen existiert bereits.")
        ClanCreationResult.ClanTagAlreadyExists -> error("Ein Clan mit diesem Tag existiert bereits.")
        ClanCreationResult.ClanAlreadyExists -> error("Ein Clan mit diesem Namen oder Tag existiert bereits.")
        else -> Unit
    }
}

private fun renderClanValidation(result: ClanValidationResult) = buildText {
    when (result) {
        is ClanValidationResult.NameOutOfRange -> {
            error("Der Name muss zwischen ")
            variableValue(result.min)
            error(" und ")
            variableValue(result.max)
            error(" Zeichen lang sein.")
        }

        is ClanValidationResult.TagOutOfRange -> {
            error("Der Tag muss zwischen ")
            variableValue(result.min)
            error(" und ")
            variableValue(result.max)
            error(" Zeichen lang sein.")
        }

        is ClanValidationResult.InvalidTagCharacters -> {
            error("Der Clan-Tag enthält ungültige Zeichen: ")
            for (entry in result.characters.char2BooleanEntrySet()) {
                val char = entry.charKey
                val valid = entry.booleanValue

                if (valid) {
                    text(char, Colors.GRAY)
                } else {
                    append {
                        text(char, Colors.ERROR)
                        decorate(TextDecoration.UNDERLINED)
                    }
                }
            }

            appendNewPrefixedLine {
                error("Er darf nur aus Buchstaben und Zahlen bestehen.")
            }
        }

        ClanValidationResult.TagViolation -> {
            error("Der Clan-Tag ist nicht erlaubt.")
        }

        else -> Unit
    }
}
