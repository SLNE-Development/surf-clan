package dev.slne.surf.clan.core.client.command

import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.invite.ClanInvite
import dev.slne.clan.api.member.ClanMember
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.clan.api.member.ClanMemberRole.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import java.util.*

class ClanAdminRulesTest {
    private fun member(role: ClanMemberRole, uuid: UUID = UUID.randomUUID()) = mockk<ClanMember> {
        every { this@mockk.uuid } returns uuid
        every { this@mockk.role } returns role
    }

    private fun clan(vararg members: ClanMember) = mockk<Clan> {
        every { this@mockk.members } returns members.toSet()
    }

    // setRoleError

    @Test
    fun `setRoleError rejects the role the member already has`() {
        val officer = member(OFFICER)
        assertEquals(ClanAdminError.ALREADY_HAS_ROLE, clan(member(OWNER), officer).setRoleError(officer, OFFICER))
    }

    @Test
    fun `setRoleError rejects demoting the only owner`() {
        val owner = member(OWNER)
        assertEquals(ClanAdminError.CANNOT_DEMOTE_LAST_OWNER, clan(owner, member(MEMBER)).setRoleError(owner, LEADER))
    }

    @Test
    fun `setRoleError allows demoting one of two owners`() {
        val owner = member(OWNER)
        assertNull(clan(owner, member(OWNER)).setRoleError(owner, LEADER))
    }

    @Test
    fun `setRoleError allows a second owner`() {
        val regular = member(MEMBER)
        assertNull(clan(member(OWNER), regular).setRoleError(regular, OWNER))
    }

    // kickError

    @Test
    fun `kickError rejects kicking the only owner`() {
        val owner = member(OWNER)
        assertEquals(ClanAdminError.CANNOT_KICK_LAST_OWNER, clan(owner, member(MEMBER)).kickError(owner))
    }

    @Test
    fun `kickError allows kicking one of two owners`() {
        val owner = member(OWNER)
        assertNull(clan(owner, member(OWNER)).kickError(owner))
    }

    @Test
    fun `kickError allows kicking a member`() {
        val regular = member(MEMBER)
        assertNull(clan(member(OWNER), regular).kickError(regular))
    }

    // transferOwnerError

    @Test
    fun `transferOwnerError rejects a clan without owner`() {
        val leader = member(LEADER)
        assertEquals(ClanAdminError.TRANSFER_NEEDS_SINGLE_OWNER, clan(leader).transferOwnerError(leader))
    }

    @Test
    fun `transferOwnerError rejects a clan with two owners`() {
        val leader = member(LEADER)
        assertEquals(
            ClanAdminError.TRANSFER_NEEDS_SINGLE_OWNER,
            clan(member(OWNER), member(OWNER), leader).transferOwnerError(leader)
        )
    }

    @Test
    fun `transferOwnerError rejects the current owner as target`() {
        val owner = member(OWNER)
        assertEquals(ClanAdminError.TARGET_ALREADY_OWNER, clan(owner, member(LEADER)).transferOwnerError(owner))
    }

    @Test
    fun `transferOwnerError allows a regular transfer`() {
        val leader = member(LEADER)
        assertNull(clan(member(OWNER), leader).transferOwnerError(leader))
    }

    // descriptionError

    @Test
    fun `descriptionError accepts the maximum length`() {
        assertNull(descriptionError("a".repeat(255)))
    }

    @Test
    fun `descriptionError rejects one character too many`() {
        assertEquals(ClanAdminError.DESCRIPTION_TOO_LONG, descriptionError("a".repeat(256)))
    }

    // parseClanRole

    @Test
    fun `parseClanRole ignores case`() {
        assertEquals(LEADER, parseClanRole("Leader"))
    }

    @Test
    fun `parseClanRole rejects unknown roles`() {
        assertNull(parseClanRole("x"))
    }

    // findMemberOf

    private val steve = member(MEMBER)
    private val alex = member(OWNER)
    private val uuids = mapOf("steve" to steve.uuid, "alex" to alex.uuid)

    /** Resolves names like the lookup service does: one name-to-uuid lookup, ignoring case. */
    private val uuidOf: suspend (String) -> UUID? = { uuids[it.lowercase()] }

    @Test
    fun `findMemberOf resolves a name with one uuid lookup`() = runBlocking {
        var lookups = 0
        val counting: suspend (String) -> UUID? = { lookups++; uuidOf(it) }

        assertSame(steve, findMemberOf(clan(steve, alex), "sTeVe", counting))
        assertEquals(1, lookups)
    }

    @Test
    fun `findMemberOf matches the uuid of a member without a lookup`() = runBlocking {
        assertSame(alex, findMemberOf(clan(steve, alex), alex.uuid.toString()) { error("no lookup expected") })
    }

    @Test
    fun `findMemberOf ignores players outside the clan`() = runBlocking {
        val outsider = UUID.randomUUID()

        assertNull(findMemberOf(clan(steve, alex), "Notch") { if (it == "Notch") outsider else null })
    }

    @Test
    fun `findMemberOf ignores the uuid of a non member`() = runBlocking {
        assertNull(findMemberOf(clan(steve, alex), UUID.randomUUID().toString(), uuidOf))
    }

    @Test
    fun `findMemberOf returns null when the name cannot be resolved`() = runBlocking {
        assertNull(findMemberOf(clan(steve, alex), "Steve") { null })
    }

    // findPendingInviteOf

    private fun invite(invited: UUID) = mockk<ClanInvite> {
        every { this@mockk.invited } returns invited
    }

    @Test
    fun `findPendingInviteOf finds an invite by the invitee name`() = runBlocking {
        val invitee = UUID.randomUUID()
        val invite = invite(invitee)
        val clan = mockk<Clan> { coEvery { getPendingInvites() } returns setOf(invite) }

        assertSame(invite, findPendingInviteOf(clan, "notch") { if (it == "notch") invitee else null })
    }

    @Test
    fun `findPendingInviteOf returns null for an unknown name`() = runBlocking {
        val clan = mockk<Clan> { coEvery { getPendingInvites() } returns setOf(invite(UUID.randomUUID())) }

        assertNull(findPendingInviteOf(clan, "Notch") { UUID.randomUUID() })
    }
}
