package dev.slne.surf.clan.core.client.command

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class DiscordInviteTest {
    @Test
    fun `accepts the invite hosts discord hands out`() {
        assertTrue(isValidDiscordInvite("https://discord.gg/castcrafter"))
        assertTrue(isValidDiscordInvite("http://discord.gg/castcrafter"))
        assertTrue(isValidDiscordInvite("discord.gg/castcrafter"))
        assertTrue(isValidDiscordInvite("https://www.discord.gg/castcrafter"))
        assertTrue(isValidDiscordInvite("https://discord.com/invite/castcrafter"))
        assertTrue(isValidDiscordInvite("https://discordapp.com/invite/castcrafter"))
    }

    @Test
    fun `accepts a trailing slash and invite codes containing a dash`() {
        assertTrue(isValidDiscordInvite("https://discord.gg/castcrafter/"))
        assertTrue(isValidDiscordInvite("https://discord.gg/cast-crafter"))
    }

    @Test
    fun `rejects anything that is not a discord invite`() {
        assertFalse(isValidDiscordInvite("https://example.com/castcrafter"))
        assertFalse(isValidDiscordInvite("https://discord.gg/"))
        assertFalse(isValidDiscordInvite("https://discord.com/castcrafter"))
        assertFalse(isValidDiscordInvite("discord.gg/cast crafter"))
        assertFalse(isValidDiscordInvite(""))
    }

    @Test
    fun `rejects an invite that carries extra path segments`() {
        assertFalse(isValidDiscordInvite("https://discord.gg/castcrafter/extra"))
    }
}
