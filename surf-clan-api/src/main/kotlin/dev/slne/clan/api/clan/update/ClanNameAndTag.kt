package dev.slne.clan.api.clan.update

import dev.slne.clan.api.clan.ClanValidationResult
import dev.slne.clan.api.util.InternalClanApi
import kotlinx.serialization.Serializable

@Serializable
data class ClanNameAndTag(
    val name: String,
    val tag: String
) {

    @ConsistentCopyVisibility
    data class Update private constructor(
        internal val name: Field<String>,
        internal val tag: Field<String>
    ) {
        class Builder @PublishedApi internal constructor() {
            private var name: Field<String> = Field.Unset
            private var tag: Field<String> = Field.Unset

            fun name(name: String) {
                this.name = Field.Set(name)
            }

            fun tag(tag: String) {
                this.tag = Field.Set(tag)
            }

            @PublishedApi
            internal fun build() = Update(name, tag)
        }

        fun hasUpdates() = name != Field.Unset || tag != Field.Unset

        @InternalClanApi
        fun changedNameOrNull() = (name as? Field.Set<String>)?.value

        @InternalClanApi
        fun changedTagOrNull() = (tag as? Field.Set<String>)?.value
    }

    @Serializable
    sealed interface UpdateResult {
        val success: Boolean

        @Serializable
        data object UpdatedName : UpdateResult {
            override val success: Boolean = true
        }

        @Serializable
        data object UpdatedTag : UpdateResult {
            override val success: Boolean = true
        }

        @Serializable
        data object UpdatedNameAndTag : UpdateResult {
            override val success: Boolean = true
        }

        @Serializable
        data object NothingChanged : UpdateResult {
            override val success: Boolean = false
        }

        @Serializable
        data object NameAlreadyTaken : UpdateResult {
            override val success: Boolean = false
        }

        @Serializable
        data object TagAlreadyTaken : UpdateResult {
            override val success: Boolean = false
        }

        @Serializable
        data object TagOrNameAlreadyTaken : UpdateResult {
            override val success: Boolean = false
        }

        @Serializable
        data class ValidationFailed(val result: ClanValidationResult) : UpdateResult {
            override val success: Boolean = false
        }
    }

    companion object {
        inline fun update(block: Update.Builder.() -> Unit): Update {
            return Update.Builder().apply(block).build()
        }
    }

    internal sealed interface Field<out T> {
        data object Unset : Field<Nothing>
        data class Set<T>(val value: T) : Field<T>
    }

    fun applyUpdate(update: Update): ClanNameAndTag {
        fun <T> Field<T>.resolve(current: T): T =
            when (this) {
                Field.Unset -> current
                is Field.Set -> value
            }

        return copy(
            name = update.name.resolve(name),
            tag = update.tag.resolve(tag)
        )
    }
}