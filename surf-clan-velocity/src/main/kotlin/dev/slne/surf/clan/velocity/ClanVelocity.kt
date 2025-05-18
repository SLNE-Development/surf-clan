package dev.slne.surf.clan.velocity

val plugin get() = ClanVelocity.INSTANCE
val container get() = plugin.container

class ClanVelocity constructor() {
    val container: Nothing? = null

    companion object {
        lateinit var INSTANCE: ClanVelocity
    }

}