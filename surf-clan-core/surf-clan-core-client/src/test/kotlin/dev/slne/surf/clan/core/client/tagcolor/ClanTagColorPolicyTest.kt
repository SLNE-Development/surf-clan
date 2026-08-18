package dev.slne.surf.clan.core.client.tagcolor

import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.clan.ClanTagColor
import net.kyori.adventure.text.format.ShadowColor
import net.kyori.adventure.text.format.TextColor
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ClanTagColorPolicyTest {
    private val custom = ClanTagColor(
        foregroundColor = TextColor.fromHexString("#FF6B6B")!!,
        backgroundColor = TextColor.fromHexString("#4ECDC4")!!,
        shadowColor = ShadowColor.shadowColor(0x11, 0x22, 0x33, 0x44)
    )

    private val allGranted = ClanTagColorGrants(foreground = true, background = true, shadow = true)
    private val noneGranted =
        ClanTagColorGrants(foreground = false, background = false, shadow = false)

    @Test
    fun `resets nothing while the owner holds every permission`() {
        val resets = ClanTagColorPolicy.resetsFor(custom, allGranted)

        assertFalse(resets.any)
    }

    @Test
    fun `resets every customized part the owner lost the permission for`() {
        val resets = ClanTagColorPolicy.resetsFor(custom, noneGranted)

        assertTrue(resets.foreground)
        assertTrue(resets.background)
        assertTrue(resets.shadow)
    }

    @Test
    fun `resets only the parts whose permission is missing`() {
        val resets = ClanTagColorPolicy.resetsFor(
            custom,
            ClanTagColorGrants(foreground = true, background = false, shadow = true)
        )

        assertFalse(resets.foreground)
        assertTrue(resets.background)
        assertFalse(resets.shadow)
    }

    @Test
    fun `never resets a part that already holds its default`() {
        val resets = ClanTagColorPolicy.resetsFor(Clan.DEFAULT_CLAN_TAG_COLORS, noneGranted)

        assertFalse(resets.any)
    }

    @Test
    fun `resets a customized part even when its siblings hold their defaults`() {
        val onlyShadowCustom = ClanTagColor(shadowColor = custom.shadowColor)

        val resets = ClanTagColorPolicy.resetsFor(onlyShadowCustom, noneGranted)

        assertFalse(resets.foreground)
        assertFalse(resets.background)
        assertTrue(resets.shadow)
    }
}
