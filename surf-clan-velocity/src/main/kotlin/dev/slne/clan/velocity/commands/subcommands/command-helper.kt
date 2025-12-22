package dev.slne.clan.velocity.commands.subcommands

import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.slne.clan.core.service.clanService

fun Argument<*>.includeClanTagSuggestions() {
    includeSuggestions(ArgumentSuggestions.stringCollection { _ -> clanService.clans.map { it.tag } })
}