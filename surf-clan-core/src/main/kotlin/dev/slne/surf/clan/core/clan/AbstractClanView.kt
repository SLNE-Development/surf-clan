package dev.slne.surf.clan.core.clan

import dev.slne.clan.api.clan.ClanView
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.surf.clan.core.config.ClanConfig
import dev.slne.surf.bitmap.common.provider.BitmapProvider
import dev.slne.surf.core.api.common.surfCoreApi
import net.kyori.adventure.text.Component
import java.util.*

abstract class AbstractClanView : ClanView {
    abstract val clanID: ULong

    override fun isMember(uuid: UUID): Boolean {
        return members.any { member -> member.uuid == uuid }
    }

    override fun hasMemberPermission(
        uuid: UUID,
        permission: ClanPermission
    ): Boolean {
        return getMember(uuid)?.hasPermission(permission) ?: false
    }

    override fun getRichClanTag(): Component = BitmapProvider.translateToComponent(
        tag,
        getClanTagColorOrDefault().foregroundColor,
        getClanTagColorOrDefault().backgroundColor,
        getClanTagColorOrDefault().shadowColor
    )

    override fun renderClanTag(minSize: Int): Component {
        if (tag.isBlank()) return Component.empty()
        if (members.size < minSize && tag !in ClanConfig.getConfig().whitelistedTags) return Component.empty()

        return getRichClanTag()
    }

    override fun broadcast(message: Component) {
        members.asSequence()
            .map { it.uuid }
            .distinct()
            .mapNotNull { surfCoreApi.getPlayer(it) }
            .forEach { surfCoreApi.sendText(it, message) }
    }
}