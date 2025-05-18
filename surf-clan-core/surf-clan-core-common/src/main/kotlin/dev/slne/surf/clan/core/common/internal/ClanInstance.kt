package dev.slne.surf.clan.core.common.internal

import dev.slne.surf.clan.ClanSpringApplication
import dev.slne.surf.cloud.api.common.CloudInstance
import org.springframework.context.ConfigurableApplicationContext

object ClanInstance {

    lateinit var context: ConfigurableApplicationContext

    fun onBootstrap() {
        context = CloudInstance.startSpringApplication(ClanSpringApplication::class.java)
    }

    fun onEnable() {

    }

}