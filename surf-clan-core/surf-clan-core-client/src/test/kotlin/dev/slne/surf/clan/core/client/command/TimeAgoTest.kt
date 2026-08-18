package dev.slne.surf.clan.core.client.command

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime
import java.time.ZoneOffset

class TimeAgoTest {
    private val now: OffsetDateTime = OffsetDateTime.of(2026, 2, 10, 19, 30, 0, 0, ZoneOffset.UTC)

    private fun ago(seconds: Long) = formatTimeAgo(now.minusSeconds(seconds), now)

    @Test
    fun `renders anything below a minute as just now`() {
        assertEquals("Gerade eben", ago(0))
        assertEquals("Gerade eben", ago(59))
    }

    @Test
    fun `renders minutes until the first full hour`() {
        assertEquals("Vor 1 Minute", ago(60))
        assertEquals("Vor 2 Minuten", ago(120))
        assertEquals("Vor 59 Minuten", ago(59 * 60))
    }

    @Test
    fun `renders hours until the first full day`() {
        assertEquals("Vor 1 Stunde", ago(3600))
        assertEquals("Vor 3 Stunden", ago(3 * 3600))
        assertEquals("Vor 23 Stunden", ago(23 * 3600))
    }

    @Test
    fun `renders days once a full day has passed`() {
        assertEquals("Vor 1 Tag", ago(24 * 3600))
        assertEquals("Vor 12 Tagen", ago(12 * 24 * 3600))
    }

    @Test
    fun `renders a timestamp from the future as just now`() {
        assertEquals("Gerade eben", formatTimeAgo(now.plusHours(5), now))
    }
}
