package dev.slne.surf.clan.core.client.command

import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.member.ClanMember
import dev.slne.clan.api.permission.ClanPermission
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.util.*

class ClanDisbandPolicyTest {
    private val owner: UUID = UUID.randomUUID()

    private fun clan(allowed: Boolean, memberCount: Int) = mockk<Clan> {
        every { hasMemberPermission(owner, ClanPermission.DISBAND) } returns allowed
        every { members } returns (1..memberCount).mapTo(mutableSetOf()) { mockk<ClanMember>() }
    }

    @Test
    fun `allows a permitted member of a small enough clan`() {
        assertNull(clan(allowed = true, memberCount = CLAN_MAX_MEMBERS_DISBAND).canBeDisbandedBy(owner))
    }

    @Test
    fun `rejects a member without the disband permission`() {
        assertEquals(
            DisbandError.NO_PERMISSION,
            clan(allowed = false, memberCount = 1).canBeDisbandedBy(owner)
        )
    }

    @Test
    fun `rejects a clan above the member limit`() {
        assertEquals(
            DisbandError.TOO_MANY_MEMBERS,
            clan(allowed = true, memberCount = CLAN_MAX_MEMBERS_DISBAND + 1).canBeDisbandedBy(owner)
        )
    }

    @Test
    fun `reports the missing permission before the member limit`() {
        assertEquals(
            DisbandError.NO_PERMISSION,
            clan(allowed = false, memberCount = CLAN_MAX_MEMBERS_DISBAND + 1).canBeDisbandedBy(owner)
        )
    }
}
