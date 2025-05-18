package dev.slne.surf.clan.velocity

import com.google.inject.Inject
import dev.slne.surf.clan.core.common.internal.ClanInstance

val plugin get() = ClanVelocity.INSTANCE
val container get() = plugin.container

class ClanVelocity @Inject constructor() {
    val container: Nothing? = null

    init {
        ClanInstance.onBootstrap()
        ClanInstance.onEnable()
    }

    companion object {
        lateinit var INSTANCE: ClanVelocity
    }

}