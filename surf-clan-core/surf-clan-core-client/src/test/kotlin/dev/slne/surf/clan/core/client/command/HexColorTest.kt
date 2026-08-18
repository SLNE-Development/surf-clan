package dev.slne.surf.clan.core.client.command

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class HexColorTest {
    @Test
    fun `accepts a hex color of the required length in either case`() {
        assertTrue(isHexColor("FF6B6B", CLAN_TAG_COLOR_LENGTH))
        assertTrue(isHexColor("ff6b6b", CLAN_TAG_COLOR_LENGTH))
        assertTrue(isHexColor("FF6B6BCC", CLAN_TAG_SHADOW_COLOR_LENGTH))
    }

    @Test
    fun `rejects a hex color of the wrong length`() {
        assertFalse(isHexColor("FF6B6B", CLAN_TAG_SHADOW_COLOR_LENGTH))
        assertFalse(isHexColor("FF6B6BCC", CLAN_TAG_COLOR_LENGTH))
        assertFalse(isHexColor("", CLAN_TAG_COLOR_LENGTH))
    }

    @Test
    fun `rejects anything that is not made of hex digits`() {
        assertFalse(isHexColor("#FF6B6B", CLAN_TAG_COLOR_LENGTH))
        assertFalse(isHexColor("GG6B6B", CLAN_TAG_COLOR_LENGTH))
        assertFalse(isHexColor("FF 6B6B", CLAN_TAG_COLOR_LENGTH))
    }
}
