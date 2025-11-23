package dev.slne.surf.clan.api.common.clan.tag

import dev.slne.surf.bitmap.common.provider.BitmapProvider
import dev.slne.surf.clan.api.common.clan.CLAN_NAME_MAX_LENGTH
import dev.slne.surf.clan.api.common.clan.CLAN_NAME_MIN_LENGTH
import dev.slne.surf.clan.api.common.clan.CLAN_TAG_MIN_LENGTH
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import dev.slne.surf.surfapi.core.api.util.objectListOf
import it.unimi.dsi.fastutil.objects.ObjectList
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.format.ShadowColor
import net.kyori.adventure.text.format.TextColor

@Serializable
data class ClanTag(
    val tag: String,
    val foregroundColor: @Contextual TextColor,
    val shadowColor: @Contextual ShadowColor?,
    val backgroundColor: @Contextual TextColor
) : ComponentLike {
    override fun asComponent() = BitmapProvider.translateToComponent(
        input = tag,
        foregroundColor = foregroundColor,
        shadowColor = shadowColor ?: ShadowColor.none(),
        backgroundColor = backgroundColor,
    )

    object Validator {
        private val LETTER_REGEX = Regex("^[A-Za-z]+$")

        fun validateTag(input: String) = validate(
            input = input,
            minLength = CLAN_TAG_MIN_LENGTH,
            maxLength = CLAN_TAG_MIN_LENGTH,
            regex = LETTER_REGEX,
            blacklist = objectListOf()
        )

        fun validateName(input: String) = validate(
            input = input,
            minLength = CLAN_NAME_MIN_LENGTH,
            maxLength = CLAN_NAME_MAX_LENGTH,
            regex = LETTER_REGEX,
            blacklist = objectListOf()
        )

        fun validate(
            input: String,
            minLength: Int,
            maxLength: Int,
            regex: Regex,
            blacklist: ObjectList<String>
        ): ClanTagValidatorResult {
            if (input.length < maxLength) {
                return ClanTagValidatorResult.TooShort
            }

            if (input.length > minLength) {
                return ClanTagValidatorResult.TooLong
            }

            if (!regex.matches(input)) {
                return ClanTagValidatorResult.InvalidCharacters
            }

            if (blacklist.any { input.contains(it, ignoreCase = true) }) {
                return ClanTagValidatorResult.Blacklisted
            }

            return ClanTagValidatorResult.Success
        }
    }

    sealed class ClanTagValidatorResult(val message: SurfComponentBuilder.() -> Unit) : ComponentLike {
        val isSuccess get() = this is Success

        override fun asComponent() = buildText(message)

        data object Success : ClanTagValidatorResult({ success("Der Clan-Tag ist gültig.") })

        data object TooShort : ClanTagValidatorResult({
            error("Der Clan-Tag ist zu kurz. Er muss mindestens ")
            variableValue(CLAN_TAG_MIN_LENGTH)
            error(" Zeichen lang sein.")
        })

        data object TooLong : ClanTagValidatorResult({
            error("Der Clan-Tag ist zu lang. Er darf maximal ")
            variableValue(CLAN_TAG_MIN_LENGTH)
            error(" Zeichen lang sein.")
        })

        data object InvalidCharacters : ClanTagValidatorResult({
            error("Der Clan-Tag enthält ungültige Zeichen. Er darf nur aus Buchstaben bestehen.")
        })

        data object Blacklisted : ClanTagValidatorResult({
            error("Der Clan-Tag enthält unzulässige Wörter oder Phrasen.")
        })
    }
}