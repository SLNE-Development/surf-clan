package dev.slne.surf.clan.core.common.internal

import com.google.auto.service.AutoService
import dev.slne.surf.clan.api.common.ClanInternalBridge
import kotlin.reflect.KClass

@AutoService(ClanInternalBridge::class)
class ClanInternalBridgeImpl : ClanInternalBridge {
    override fun <B : Any> getBean(clazz: KClass<B>) = getJBean(clazz.java)
    override fun <B : Any> getJBean(clazz: Class<B>) = ClanInstance.context.getBean(clazz)
}