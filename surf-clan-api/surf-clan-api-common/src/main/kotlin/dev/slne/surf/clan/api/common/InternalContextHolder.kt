@file:OptIn(InternalClanApi::class)

package dev.slne.surf.clan.api.common

import dev.slne.surf.clan.api.common.util.InternalClanApi
import dev.slne.surf.surfapi.core.api.util.requiredService
import org.springframework.context.ApplicationContext

private val contextHolder = requiredService<InternalContextHolder>()

@InternalClanApi
interface InternalContextHolder {
    val context: ApplicationContext

    companion object : InternalContextHolder by contextHolder {
        val INSTANCE get() = contextHolder
    }
}