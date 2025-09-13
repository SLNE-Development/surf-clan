package dev.slne.surf.clan.api.common.clan.result

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component

@Serializable
abstract class ClanSetNameResult {
    protected abstract suspend fun SurfComponentBuilder.buildMessage()

    suspend fun asComponent(): Component {
        val componentBuilder = SurfComponentBuilder.builder()
        componentBuilder.buildMessage()
        return componentBuilder.build()
    }

    data class Success(
        val clan: Clan, val oldName: String, val newName: String
    ) : ClanSetNameResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            success("Du hast den Clan-Namen von ")
            variableValue(oldName)
            success(" zu ")
            variableValue(newName)
            success(" geändert.")
        }
    }

    data class ClanNotFound(val clan: Clan) : ClanSetNameResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Der Clan ")
            append(clan)
            error(" wurde nicht in der Datenbank gefunden.")
        }
    }

    data class NotClanMember(val clan: Clan) : ClanSetNameResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Du bist kein Mitglied des Clans ")
            append(clan)
            error(" und kannst daher den Namen nicht ändern.")
        }
    }

    data class NameDoesntMatchLength(
        val name: String, val minLength: Int, val maxLength: Int
    ) : ClanSetNameResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Der Clan-Name ")
            variableValue(name)
            error(" muss zwischen ")
            variableValue(minLength.toString())
            error(" und ")
            variableValue(maxLength.toString())
            error(" Zeichen lang sein.")
        }
    }

    data class NoPermission(val clan: Clan) : ClanSetNameResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Du hast keine Berechtigung, um den Namen des Clans ")
            append(clan)
            error(" zu ändern.")
        }
    }

    data class NameAlreadyInUse(val name: String) : ClanSetNameResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Der Clan-Name ")
            variableValue(name)
            error(" ist bereits vergeben.")
        }
    }

}