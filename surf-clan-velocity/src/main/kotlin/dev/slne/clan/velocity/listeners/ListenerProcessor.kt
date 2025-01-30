package dev.slne.clan.velocity.listeners

import com.velocitypowered.api.event.Subscribe
import dev.slne.clan.core.utils.containsMethodWithAnnotation
import dev.slne.clan.core.utils.isCandidateFor
import dev.slne.clan.core.utils.ultimateTargetClass
import dev.slne.clan.velocity.plugin
import dev.slne.surf.surfapi.core.api.util.logger
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import org.springframework.aop.framework.AopInfrastructureBean
import org.springframework.beans.BeansException
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.stereotype.Component

@Component
class ListenerProcessor : BeanPostProcessor {

    @Throws(BeansException::class)
    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
        if (bean is AopInfrastructureBean) return bean

        val targetClass = bean.ultimateTargetClass()

        if (targetClass.isCandidateFor<Subscribe>() && targetClass.containsMethodWithAnnotation<Subscribe>()) {
            candidates.add(bean)
        }

        return bean
    }

    companion object {
        private val log = logger()
        private val candidates = mutableObjectSetOf<Any>()

        internal fun registerListeners() {
            for (listener in candidates) {
                try {
                    plugin.eventManager.register(plugin, listener)
                } catch (e: Throwable) {
                    log.atWarning()
                        .withCause(e)
                        .log("Failed to register listener $listener continuing with next listener")
                }
            }
        }
    }
}