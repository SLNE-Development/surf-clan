package dev.slne.clan.paper.tagcolor

import com.github.benmanes.caffeine.cache.Caffeine
import com.sksamuel.aedile.core.expireAfterWrite
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.clan.paper.permission.ClanPermissions
import dev.slne.surf.api.core.luckperms.LuckPermsAccess
import dev.slne.surf.api.core.util.logger
import net.luckperms.api.cacheddata.CachedPermissionData
import java.util.*
import kotlin.time.Duration.Companion.minutes

object ClanTagColorEnforcer {
    private val log = logger()

    private val recentlyChecked = Caffeine.newBuilder()
        .expireAfterWrite(15.minutes)
        .maximumSize(10_000)
        .build<UUID, Boolean>()

    suspend fun enforceForClanOf(playerUuid: UUID) {
        try {
            val clan = Clan.byPlayer(playerUuid) ?: return
            enforce(clan)
        } catch (e: Throwable) {
            log.atWarning()
                .withCause(e)
                .log("Failed to check the clan tag color for player %s", playerUuid)
        }
    }

    private suspend fun enforce(clan: Clan) {
        if (recentlyChecked.asMap().putIfAbsent(clan.uuid, true) != null) return

        val current = clan.getClanTagColorOrDefault()
        if (current == Clan.DEFAULT_CLAN_TAG_COLORS) return

        val owner = clan.members.find { member -> member.role == ClanMemberRole.OWNER }
        if (owner == null) {
            log.atWarning().log("Clan %s has no owner, skipping tag color check", clan.tag)
            return
        }

        val resets = ClanTagColorPolicy.resetsFor(current, grantsOf(owner.uuid))
        if (!resets.any) return

        clan.changeClanTagColor(resets.toUpdate())

        log.atInfo().log(
            "Reset tag color of clan %s because its owner lacks the required permissions: %s",
            clan.tag,
            resets
        )
    }

    private suspend fun grantsOf(ownerUuid: UUID): ClanTagColorGrants {
        val cachedUser = LuckPermsAccess.getUser(ownerUuid)
        val user = cachedUser ?: LuckPermsAccess.loadUser(ownerUuid)

        try {
            val permissions = user.cachedData.permissionData

            return ClanTagColorGrants(
                foreground = permissions.grants(
                    ClanPermissions.CLAN_OPTIONS_TAG_COLOR_FOREGROUND_COMMAND
                ),
                background = permissions.grants(
                    ClanPermissions.CLAN_OPTIONS_TAG_COLOR_BACKGROUND_COMMAND
                ),
                shadow = permissions.grants(
                    ClanPermissions.CLAN_OPTIONS_TAG_COLOR_SHADOW_COMMAND
                )
            )
        } finally {
            if (cachedUser == null) {
                LuckPermsAccess.luckperms.userManager.cleanupUser(user)
            }
        }
    }

    private fun CachedPermissionData.grants(permission: String) =
        checkPermission(permission.lowercase()).asBoolean()
}
