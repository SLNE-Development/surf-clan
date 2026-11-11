package dev.slne.surf.clan.core.client.command

import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.member.ClanMember
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.clan.api.member.ClanMemberRole.LEADER
import dev.slne.clan.api.member.ClanMemberRole.OWNER
import io.mockk.*
import kotlinx.coroutines.runBlocking
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.*

class ClanAdminTransferOwnerTest {
    private val actor = ClanAdminActor("Admin", UUID.randomUUID())

    private fun member(role: ClanMemberRole, changes: Boolean = true) = mockk<ClanMember> {
        every { uuid } returns UUID.randomUUID()
        every { this@mockk.role } returns role
        coEvery { changeRole(any()) } returns changes
    }

    private fun clan(vararg members: ClanMember) = mockk<Clan> {
        every { this@mockk.members } returns members.toSet()
        every { tag } returns "WAR"
        every { uuid } returns UUID.randomUUID()
        every { getClanTagColorOrDefault() } returns Clan.DEFAULT_CLAN_TAG_COLORS
        every { broadcast(any()) } just Runs
    }

    private class Names(vararg named: Pair<ClanMember, String>) {
        private val byUuid = named.associate { (member, name) -> member.uuid to name }
        private val byName = named.associate { (member, name) -> name.lowercase() to member.uuid }

        val nameOf: suspend (UUID) -> String? = { byUuid[it] }
        val uuidOf: suspend (String) -> UUID? = { byName[it.lowercase()] }
    }

    private suspend fun transfer(clan: Clan, target: String, names: Names) =
        adminTransferOwner(actor, clan, target, names.nameOf, names.uuidOf)

    private fun plain(component: Component) = PlainTextComponentSerializer.plainText().serialize(component)

    @Test
    fun `promotes the target before demoting the old owner`() = runBlocking {
        val owner = member(OWNER)
        val leader = member(LEADER)

        val result = transfer(clan(owner, leader), "Leader", Names(owner to "Owner", leader to "Leader"))

        assertInstanceOf(ClanAdminResult.Success::class.java, result)
        coVerifyOrder {
            leader.changeRole(OWNER)
            owner.changeRole(LEADER)
        }
    }

    @Test
    fun `stops when the promotion fails`() = runBlocking {
        val owner = member(OWNER)
        val leader = member(LEADER, changes = false)
        val clan = clan(owner, leader)

        val result = transfer(clan, "Leader", Names(owner to "Owner", leader to "Leader"))

        assertEquals(ClanAdminResult.Failure(ROLE_CHANGE_FAILED), result)
        coVerify(exactly = 0) { owner.changeRole(any()) }
        verify(exactly = 0) { clan.broadcast(any()) }
    }

    @Test
    fun `reports two owners when the demotion fails`() = runBlocking {
        val owner = member(OWNER, changes = false)
        val leader = member(LEADER)
        val clan = clan(owner, leader)

        val result = transfer(clan, "Leader", Names(owner to "Owner", leader to "Leader"))

        val success = assertInstanceOf(ClanAdminResult.Success::class.java, result)
        assertTrue(plain(success.message).contains("setrole"))
        val broadcast = slot<Component>()
        verify(exactly = 1) { clan.broadcast(capture(broadcast)) }
        assertFalse(plain(broadcast.captured).contains("ist jetzt"))
    }

    @Test
    fun `reports two owners when the demotion throws`() = runBlocking {
        val owner = member(OWNER)
        coEvery { owner.changeRole(LEADER) } throws IllegalStateException("rpc unavailable")
        val leader = member(LEADER)
        val clan = clan(owner, leader)

        val result = transfer(clan, "Leader", Names(owner to "Owner", leader to "Leader"))

        val success = assertInstanceOf(ClanAdminResult.Success::class.java, result)
        assertTrue(plain(success.message).contains("setrole"))
        verify(exactly = 1) { clan.broadcast(any()) }
    }

    @Test
    fun `rejects a clan with two owners`() = runBlocking {
        val first = member(OWNER)
        val second = member(OWNER)
        val leader = member(LEADER)

        val result = transfer(
            clan(first, second, leader),
            "Leader",
            Names(first to "First", second to "Second", leader to "Leader")
        )

        assertEquals(ClanAdminResult.Failure(ClanAdminError.TRANSFER_NEEDS_SINGLE_OWNER.message), result)
        coVerify(exactly = 0) { leader.changeRole(any()) }
    }
}
