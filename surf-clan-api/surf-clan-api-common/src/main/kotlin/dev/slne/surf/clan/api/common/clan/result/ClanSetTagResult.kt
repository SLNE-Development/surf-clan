package dev.slne.surf.clan.api.common.clan.result

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.tag.ClanTag
import dev.slne.surf.clan.api.common.util.ComponentResult
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
abstract class ClanSetTagResult : ComponentResult {
    override val isSuccess get() = this is Success

    data class Success(
        val clan: Clan,
        val playerUuid: UUID,
        val oldTag: ClanTag,
        val newTag: ClanTag
    ) : ClanSetTagResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            success("Du hast den Clan-Tag von ")
            append(oldTag)
            success(" zu ")
            append(newTag)
            success(" geändert.")
        }
    }

    data class InvalidTag(val tag: String) : ClanSetTagResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Der Clan-Tag ")
            variableValue(tag)
            error(" ist ungültig.")
        }
    }

    data class BlacklistedTag(
        val tag: String,
        val category: String,
        val description: String?
    ) : ClanSetTagResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Der Clan-Tag ")
            variableValue("$category:$tag")
            error(" ist nicht erlaubt.")

            description?.let {
                appendSpace()
                error("Grund:  ")
                variableValue(it)
            }
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

    data class TagAlreadyInUse(val tag: String) : ClanSetTagResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Der Clan-Tag ")
            variableValue(tag)
            error(" ist bereits vergeben.")
        }
    }

}