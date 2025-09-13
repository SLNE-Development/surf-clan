@file:OptIn(InternalClanApi::class)

package dev.slne.surf.clan.core.common

import com.google.auto.service.AutoService
import dev.slne.surf.clan.api.common.InternalContextHolder
import dev.slne.surf.clan.api.common.util.InternalClanApi
import net.kyori.adventure.util.Services
import org.springframework.context.ApplicationContext

@AutoService(InternalContextHolder::class)
class InternalContextHolderImpl : InternalContextHolder, Services.Fallback {
    override lateinit var context: ApplicationContext

    companion object {
        val INSTANCE = InternalContextHolder.INSTANCE as InternalContextHolderImpl
    }
}