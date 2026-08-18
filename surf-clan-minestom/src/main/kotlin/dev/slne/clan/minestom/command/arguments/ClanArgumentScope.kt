package dev.slne.clan.minestom.command.arguments

import dev.slne.surf.api.core.util.logger
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

private val log = logger()

internal val clanArgumentScope = CoroutineScope(
    Dispatchers.Default +
            CoroutineName("ClanArguments") +
            CoroutineExceptionHandler { _, throwable ->
                log.atWarning()
                    .withCause(throwable)
                    .log("An error occurred while resolving a clan command argument")
            }
)
