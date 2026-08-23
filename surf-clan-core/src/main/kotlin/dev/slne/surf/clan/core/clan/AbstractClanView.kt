package dev.slne.surf.clan.core.clan

import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.clan.ClanView
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.surf.bitmap.common.provider.BitmapProvider
import dev.slne.surf.clan.core.config.ClanConfig
import dev.slne.surf.core.api.common.SurfCoreApi
import net.kyori.adventure.text.Component
import java.time.OffsetDateTime
import java.util.*

abstract class AbstractClanView : ClanView {
    abstract val clanID: ULong

    override val activeMemberCount: Int
        get() {
            val cutoff = OffsetDateTime.now().minusSeconds(Clan.INACTIVE_AFTER.inWholeSeconds)
            return members.count { !it.lastActiveAt.isBefore(cutoff) }
        }

    override fun isMember(uuid: UUID): Boolean {
        return members.any { member -> member.uuid == uuid }
    }

    override fun hasMemberPermission(
        uuid: UUID,
        permission: ClanPermission
    ): Boolean {
        return getMember(uuid)?.hasPermission(permission) ?: false
    }

    override fun getRichClanTag(): Component {
        val color = getClanTagColorOrDefault()

        return BitmapProvider.translateToComponent(
            tag,
            color.foregroundColor,
            color.backgroundColor,
            color.shadowColor
        )
    }

    override fun renderClanTag(minSize: Int): Component {
        if (tag.isBlank()) return Component.empty()
        if (activeMemberCount < minSize && tag !in ClanConfig.getConfig().whitelistedTags) {
            return Component.empty()
        }

        return getRichClanTag()
    }

    override fun broadcast(message: Component) {
        members.asSequence()
            .map { it.uuid }
            .distinct()
            .mapNotNull { SurfCoreApi.getPlayer(it) }
            .forEach { SurfCoreApi.sendText(it, message) }
    }
}