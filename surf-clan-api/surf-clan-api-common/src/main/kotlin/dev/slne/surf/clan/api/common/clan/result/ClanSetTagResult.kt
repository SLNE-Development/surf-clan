package dev.slne.surf.clan.api.common.clan.result

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.tag.ClanTag
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component

@Serializable
abstract class ClanSetTagResult {
    protected abstract suspend fun SurfComponentBuilder.buildMessage()

    suspend fun asComponent(): Component {
        val componentBuilder = SurfComponentBuilder.builder()
        componentBuilder.buildMessage()
        return componentBuilder.build()
    }

    data class Success(
        val clan: Clan, val oldTag: ClanTag, val newTag: ClanTag
    ) : ClanSetTagResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            success("Du hast den Clan-Tag von ")
            append(oldTag)
            success(" zu ")
            append(newTag)
            success(" geändert.")
        }
    }

    data class ClanNotFound(val clan: Clan) : ClanSetTagResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Der Clan ")
            append(clan)
            error(" wurde nicht in der Datenbank gefunden.")
        }
    }

    data class InvalidTag(val tag: String) : ClanSetTagResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Der Clan-Tag ")
            variableValue(tag)
            error(" ist ungültig.")
        }
    }

    data class TagDoesntMatchLength(
        val tag: String, val minLength: Int, val maxLength: Int
    ) : ClanSetTagResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Der Clan-Tag ")
            variableValue(tag)
            error(" muss zwischen ")
            variableValue(minLength.toString())
            error(" und ")
            variableValue(maxLength.toString())
            error(" Zeichen lang sein.")
        }
    }

    data class NotClanMember(val clan: Clan) : ClanSetTagResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Du bist kein Mitglied des Clans ")
            append(clan)
            error(" und kannst daher den Tag nicht ändern.")
        }
    }

    data class NoPermission(val clan: Clan) : ClanSetTagResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Du hast keine Berechtigung, um den Tag des Clans ")
            append(clan)
            error(" zu ändern.")
        }
    }

    data class TagAlreadyInUse(val tag: String) : ClanSetTagResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Der Clan-Tag ")
            variableValue(tag)
            error(" ist bereits vergeben.")
        }
    }

}