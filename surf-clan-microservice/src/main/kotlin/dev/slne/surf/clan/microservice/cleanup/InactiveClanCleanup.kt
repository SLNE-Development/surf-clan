package dev.slne.surf.clan.microservice.cleanup

import dev.slne.surf.api.core.util.logger
import dev.slne.surf.clan.core.config.ClanConfig
import dev.slne.surf.clan.microservice.db.repository.ClanRepository
import dev.slne.surf.clan.microservice.db.repository.DeletableClan
import kotlinx.coroutines.CancellationException

/**
 * Deletes clans that can no longer free themselves: those without members, and those whose members
 * have all been offline for longer than [dev.slne.clan.api.clan.Clan.INACTIVE_AFTER].
 *
 * Runs on the microservice only, because that is the one place in the network where it runs exactly
 * once. Its collaborators are parameters with defaults so the decision logic can be exercised
 * without a database or a service registry behind it.
 */
object InactiveClanCleanup {
    private val log = logger()

    suspend fun run(
        cleanup: ClanConfig.Cleanup = ClanConfig.getConfig().cleanup,
        findDeletable: suspend () -> List<DeletableClan> = { ClanRepository.findDeletableClans() },
        deleteStillDeletable: suspend (Collection<ULong>) -> Int = {
            ClanRepository.deleteClansStillDeletable(it)
        }
    ) {
        if (!cleanup.enabled) return

        try {
            val candidates = findDeletable()

            if (candidates.isEmpty()) {
                // Fine, not info: nothing to delete is the normal outcome once the backlog is gone,
                // and an hourly info line about it would only train people to ignore the log.
                log.atFine().log("Clan cleanup found nothing to delete")
                return
            }

            for (candidate in candidates) {
                log.atInfo().log(
                    "Clan cleanup candidate id=%s tag=%s name='%s' members=%s lastActivity=%s reason=%s",
                    candidate.clanID,
                    candidate.tag,
                    candidate.name,
                    candidate.memberCount,
                    candidate.lastActivityAt,
                    candidate.reason
                )
            }

            if (cleanup.dryRun) {
                log.atInfo().log(
                    "Clan cleanup dry run: %s clans would be deleted, nothing was",
                    candidates.size
                )
                return
            }

            val deleted = deleteStillDeletable(candidates.map { it.clanID })

            // Fewer deleted than listed means a member logged in between the two queries and the
            // subquery in the delete let its clan survive. Expected, so it is logged as an outcome
            // rather than a problem.
            log.atInfo().log("Clan cleanup deleted %s of %s candidates", deleted, candidates.size)
        } catch (e: CancellationException) {
            // Must not be swallowed with the rest: eating this would break cancellation of the
            // scheduled job on shutdown.
            throw e
        } catch (e: Throwable) {
            // Swallowed on purpose. Letting this escape would end the scheduled job, so a single bad
            // run would stop the cleanup permanently instead of retrying on the next tick.
            log.atWarning().withCause(e).log("Clan cleanup run failed")
        }
    }
}
