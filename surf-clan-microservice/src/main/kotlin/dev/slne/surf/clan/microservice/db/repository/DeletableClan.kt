package dev.slne.surf.clan.microservice.db.repository

import java.time.OffsetDateTime

/**
 * A clan the cleanup may delete, together with the numbers its log line needs.
 *
 * The numbers travel out of the query because they are gone the moment the clan is deleted — after
 * the fact there is no way to say how many members it had or when it was last alive.
 */
data class DeletableClan(
    val clanID: ULong,
    val name: String,
    val tag: String,
    val memberCount: Long,
    val lastActivityAt: OffsetDateTime?,
) {
    /**
     * Why this clan qualifies.
     *
     * Derived rather than stored: a clan without members has no activity to judge, which is exactly
     * the case where [lastActivityAt] is `null`.
     */
    val reason: Reason
        get() = if (memberCount == 0L) Reason.NO_MEMBERS else Reason.ALL_MEMBERS_INACTIVE

    enum class Reason { NO_MEMBERS, ALL_MEMBERS_INACTIVE }
}
