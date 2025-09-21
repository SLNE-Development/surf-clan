package dev.slne.surf.clan.api.common.util

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.member.role.permission.ClanPermission
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import net.kyori.adventure.text.Component
import java.util.*
import kotlin.reflect.KClass

interface ComponentResult {

    val isSuccess: Boolean
    val isError: Boolean get() = !isSuccess

    suspend fun SurfComponentBuilder.buildMessage()

    suspend fun asComponent(): Component {
        val componentBuilder = SurfComponentBuilder.builder()
        componentBuilder.buildMessage()
        return componentBuilder.build()
    }

    data object EmptySuccess : ComponentResult {
        override val isSuccess = true

        override suspend fun SurfComponentBuilder.buildMessage() {}
    }

    data class PlayerNotFound(val player: ClanPlayer) : ComponentResult {
        override val isSuccess = false

        override suspend fun SurfComponentBuilder.buildMessage() {
            append(player.asComponent())
            error(" konnte nicht gefunden werden.")
        }
    }

    data class NoPolicyFound(
        val clan: Clan,
        val playerUuid: UUID,
        val actionClass: KClass<out ClanAction<*>>,
    ) : ComponentResult {
        override val isSuccess = false

        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Es wurde keine Berechtigungsrichtlinie für die Aktion ")
            variableValue(actionClass.simpleName ?: actionClass.toString())
            error(" im Clan ")
            append(clan)
            error(" gefunden. Bitte kontaktiere einen Administrator.")
        }
    }

    data class SelfNotClanMember(
        val clan: Clan,
        val playerUuid: UUID
    ) : ComponentResult {
        override val isSuccess = false

        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Du bist kein Mitglied des Clans ")
            append(clan)
            error(" und kannst daher keine Aktionen in diesem durchführen.")
        }
    }

    data class NoPermissions(
        val clan: Clan,
        val playerUuid: UUID,
        val permission: ClanPermission
    ) : ComponentResult {
        override val isSuccess = false

        override suspend fun SurfComponentBuilder.buildMessage() {
            val player = ClanPlayer[playerUuid]

            append(permission.asComponent(player))
        }
    }

    data class OtherNotClanMember(
        val clan: Clan,
        val playerUuid: UUID,
        val targetUuid: UUID
    ) : ComponentResult {
        override val isSuccess = false

        override suspend fun SurfComponentBuilder.buildMessage() {
            val player = ClanPlayer[targetUuid]

            append(player.asComponent())
            error(" ist kein Mitglied des Clans ")
            append(clan)
            error(".")
        }
    }

    data class ClanNotFound(val clan: Clan) : ComponentResult {
        override val isSuccess = false

        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Der Clan ")
            append(clan)
            error(" wurde nicht in der Datenbank gefunden.")
        }
    }

}