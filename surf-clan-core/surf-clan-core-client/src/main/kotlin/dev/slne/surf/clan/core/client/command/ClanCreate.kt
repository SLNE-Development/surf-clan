package dev.slne.surf.clan.core.client.command

import dev.slne.clan.api.clan.ClanCreationResult
import dev.slne.clan.api.clan.ClanValidationResult
import dev.slne.surf.api.core.messages.Colors
import dev.slne.surf.api.core.messages.adventure.buildText
import dev.slne.surf.api.core.messages.builder.SurfComponentBuilder
import dev.slne.surf.clan.core.clan.ClanImpl
import dev.slne.surf.clan.core.client.Messages
import dev.slne.surf.clan.core.client.components.Components
import net.kyori.adventure.text.format.TextDecoration

/**
 * Reports that the clan was created, with its information available on hover.
 */
suspend fun clanCreatedMessage(clan: ClanImpl) = buildText {
    appendSuccessPrefix()
    success("Der Clan ")
    append(Components.Clan.renderClanInformationHover(clan))
    success(" wurde erfolgreich erstellt.")
}

/**
 * Reports why creating a clan was rejected.
 */
fun clanCreationFailedMessage(result: ClanCreationResult) = buildText {
    appendErrorPrefix()
    append(renderClanCreationResult(result))
}

private fun renderClanCreationResult(result: ClanCreationResult) = buildText {
    when (result) {
        is ClanCreationResult.InvalidTagOrName -> append(renderClanValidation(result.reason))
        ClanCreationResult.OwnerIsAlreadyInClan -> error(Messages.ALREADY_IN_CLAN)
        ClanCreationResult.ClanNameAlreadyExists -> error("Ein Clan mit diesem Namen existiert bereits.")
        ClanCreationResult.ClanTagAlreadyExists -> error("Ein Clan mit diesem Tag existiert bereits.")
        ClanCreationResult.ClanAlreadyExists -> error("Ein Clan mit diesem Namen oder Tag existiert bereits.")
        else -> Unit
    }
}

private fun renderClanValidation(result: ClanValidationResult) = buildText {
    when (result) {
        is ClanValidationResult.NameOutOfRange -> appendOutOfRange("Der Name", result.min, result.max)
        is ClanValidationResult.TagOutOfRange -> appendOutOfRange("Der Tag", result.min, result.max)

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

            appendNewErrorPrefixedLine()
            error("Er darf nur aus Buchstaben und Zahlen bestehen.")
        }

        ClanValidationResult.TagViolation -> {
            error("Der Clan-Tag ist nicht erlaubt.")
        }

        else -> Unit
    }
}

private fun SurfComponentBuilder.appendOutOfRange(subject: String, min: Int, max: Int) {
    error("$subject muss zwischen ")
    variableValue(min)
    error(" und ")
    variableValue(max)
    error(" Zeichen lang sein.")
}
