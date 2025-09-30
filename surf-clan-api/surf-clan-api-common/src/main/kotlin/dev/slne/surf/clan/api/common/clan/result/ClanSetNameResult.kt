package dev.slne.surf.clan.api.common.clan.result

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.util.ComponentResult
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
abstract class ClanSetNameResult : ComponentResult {
    override val isSuccess get() = this is Success

    data class Success(
        val clan: Clan,
        val playerUuid: UUID,
        val oldName: String,
        val newName: String
    ) : ClanSetNameResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            success("Du hast den Clan-Namen von ")
            variableValue(oldName)
            success(" zu ")
            variableValue(newName)
            success(" geändert.")
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

    data class NameAlreadyInUse(val name: String) : ClanSetNameResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Der Clan-Name ")
            variableValue(name)
            error(" ist bereits vergeben.")
        }
    }

}