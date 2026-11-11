package dev.slne.surf.clan.core.client.command

import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.member.ClanMember
import dev.slne.clan.api.member.ClanMemberRole
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.util.*

class ClanLeavePolicyTest {
    private val player: UUID = UUID.randomUUID()

    private fun clan(role: ClanMemberRole, creator: UUID): Clan {
        val member = mockk<ClanMember> { every { this@mockk.role } returns role }

        return mockk<Clan> {
            every { createdByUuid } returns creator
            every { getMember(player) } returns member
        }
    }

    @Test
    fun `blocks an owner who did not create the clan`() {
        assertEquals(
            CLAN_OWNER_CANNOT_LEAVE,
            clan(ClanMemberRole.OWNER, UUID.randomUUID()).cannotLeaveReason(player)
        )
    }

    @Test
    fun `lets the creator leave once they are no longer owner`() {
        assertNull(clan(ClanMemberRole.LEADER, player).cannotLeaveReason(player))
    }

    @Test
    fun `lets a regular member leave`() {
        assertNull(clan(ClanMemberRole.MEMBER, UUID.randomUUID()).cannotLeaveReason(player))
    }
}
