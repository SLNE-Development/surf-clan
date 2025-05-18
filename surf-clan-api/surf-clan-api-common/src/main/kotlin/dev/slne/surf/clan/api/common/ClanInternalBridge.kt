package dev.slne.surf.clan.api.common

import dev.slne.surf.surfapi.core.api.util.requiredService
import kotlin.reflect.KClass

interface ClanInternalBridge {

    fun <B : Any> getBean(clazz: KClass<B>): B
    fun <B : Any> getJBean(clazz: Class<B>): B

    companion object {
        internal val instance = requiredService<ClanInternalBridge>()
    }

}