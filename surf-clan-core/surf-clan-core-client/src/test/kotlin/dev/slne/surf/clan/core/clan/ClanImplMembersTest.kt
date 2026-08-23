package dev.slne.surf.clan.core.clan

import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.surf.clan.core.member.ClanMemberImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime
import java.util.*
import java.util.concurrent.CyclicBarrier
import kotlin.concurrent.thread

/**
 * A cached clan is shared by every thread that reads the same cache entry, so the compound updates of
 * its member set have to be atomic. Concurrency tests cannot prove race freedom, but the unsynchronized
 * `members = members + member` these replaced loses updates in this shape almost every run.
 */
class ClanImplMembersTest {
    private fun member(role: ClanMemberRole = ClanMemberRole.MEMBER) = ClanMemberImpl(
        ID = 0uL,
        uuid = UUID.randomUUID(),
        role = role,
        addedBy = null
    )

    private fun clan(members: Set<ClanMemberImpl>) = ClanImpl(
        clanID = 1uL,
        uuid = UUID.randomUUID(),
        name = "Warriors",
        tag = "WAR",
        createdByUuid = UUID.randomUUID(),
        clanTagColor = null,
        description = null,
        discordInvite = null,
        members = members,
        updatedAt = OffsetDateTime.now(),
        createdAt = OffsetDateTime.now()
    )

    /** Runs [block] for every index in parallel, all released at the same time. */
    private fun inParallel(count: Int, block: (Int) -> Unit) {
        val barrier = CyclicBarrier(count)

        (0 until count)
            .map { index ->
                thread {
                    barrier.await()
                    block(index)
                }
            }
            .forEach { it.join() }
    }

    @RepeatedTest(20)
    fun `keeps every concurrently added member`() {
        val clan = clan(emptySet())
        val added = List(32) { member() }

        inParallel(added.size) { index -> clan.addMemberLocally(added[index]) }

        assertEquals(added.size, clan.members.size)
        assertTrue(clan.members.containsAll(added))
    }

    @RepeatedTest(20)
    fun `removes every concurrently removed member`() {
        val members = List(32) { member() }
        val clan = clan(members.toSet())

        inParallel(members.size) { index -> clan.removeMemberLocally(members[index].uuid) }

        assertTrue(clan.members.isEmpty())
    }

    @RepeatedTest(20)
    fun `keeps additions and removals of different members independent`() {
        val staying = List(16) { member() }
        val leaving = List(16) { member() }
        val joining = List(16) { member() }
        val clan = clan((staying + leaving).toSet())

        inParallel(leaving.size + joining.size) { index ->
            if (index < leaving.size) {
                clan.removeMemberLocally(leaving[index].uuid)
            } else {
                clan.addMemberLocally(joining[index - leaving.size])
            }
        }

        assertEquals(staying.size + joining.size, clan.members.size)
        assertTrue(clan.members.containsAll(staying))
        assertTrue(clan.members.containsAll(joining))
    }

    @Test
    fun `publishes a set the caller cannot mutate`() {
        val clan = clan(emptySet())
        clan.addMemberLocally(member())

        val published = clan.members

        @Suppress("UNCHECKED_CAST")
        val mutable = published as MutableSet<ClanMemberImpl>
        val rejected = runCatching { mutable.add(member()) }.isFailure

        assertTrue(rejected)
        assertEquals(1, published.size)
    }

    @Test
    fun `leaves the previous snapshot untouched`() {
        val first = member()
        val clan = clan(emptySet())
        clan.addMemberLocally(first)

        val snapshot = clan.members
        clan.addMemberLocally(member())

        assertEquals(1, snapshot.size)
        assertEquals(2, clan.members.size)
    }

    @Test
    fun `counts only members seen within the inactivity window`() {
        val now = OffsetDateTime.now()
        val threshold = Clan.INACTIVE_AFTER.inWholeSeconds
        // A minute of slack on either side of the threshold, so the clock advancing during the test
        // cannot flip a member across it.
        val members = setOf(
            member().copy(uuid = UUID.randomUUID(), lastActiveAt = now.minusMinutes(1)),
            member().copy(uuid = UUID.randomUUID(), lastActiveAt = now.minusSeconds(threshold - 60)),
            member().copy(uuid = UUID.randomUUID(), lastActiveAt = now.minusSeconds(threshold + 60))
        )

        assertEquals(2, clan(members).activeMemberCount)
    }
}
