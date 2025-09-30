package dev.slne.surf.clan.core.common.clan.actions

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.util.ClanAction
import dev.slne.surf.clan.api.common.util.ComponentResult
import dev.slne.surf.clan.core.common.utils.ClanActionCommon
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

@Component
class ClanActionProcessor : BeanPostProcessor {

    private val actions = mutableListOf<ClanActionCommon<*>>()

    override fun postProcessAfterInitialization(
        bean: Any,
        beanName: String
    ): Any {
        if (bean !is ClanActionCommon<*>) {
            return bean
        }

        actions.add(bean)

        return bean
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun <Action : ClanAction<out Arguments>, Arguments : Any> authorize(
        action: KClass<out Action>,
        clan: Clan,
        player: ClanPlayer,
        arguments: Arguments
    ): ComponentResult {
        val action = (actions.firstOrNull {
            it::class.java.isAssignableFrom(action.java)
        } ?: return ComponentResult.NoPolicyFound(
            clan,
            player.uuid,
            action
        )) as ClanActionCommon<Arguments>

        return action.authorize(clan, player, arguments)
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun <Action : ClanAction<out Arguments>, Arguments : Any> execute(
        action: KClass<out Action>,
        clan: Clan,
        player: ClanPlayer,
        arguments: Arguments
    ): ComponentResult {
        val action = (actions.firstOrNull {
            it::class.java.isAssignableFrom(action.java)
        } ?: return ComponentResult.NoPolicyFound(
            clan,
            player.uuid,
            action
        )) as ClanActionCommon<Arguments>

        return action.execute(clan, player, arguments)
    }
}

suspend inline fun <reified Action : ClanAction<out Arguments>, Arguments : Any> ClanActionProcessor.execute(
    clan: Clan,
    player: ClanPlayer,
    arguments: Arguments
) = execute(
    Action::class,
    clan,
    player,
    arguments
)