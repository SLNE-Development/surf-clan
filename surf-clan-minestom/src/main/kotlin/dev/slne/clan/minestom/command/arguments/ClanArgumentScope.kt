package dev.slne.clan.minestom.command.arguments

import dev.slne.surf.api.core.util.logger
import kotlinx.coroutines.*

private val log = logger()

internal val clanArgumentScope = CoroutineScope(
    SupervisorJob() +
            Dispatchers.Default +
            CoroutineName("ClanArguments") +
            CoroutineExceptionHandler { _, throwable ->
                log.atWarning()
                    .withCause(throwable)
                    .log("An error occurred while resolving a clan command argument")
            }
)
