package dev.slne.clan.velocity.extensions

import com.velocitypowered.api.proxy.Player
import dev.slne.clan.core.COLOR_VARIABLE
import net.kyori.adventure.text.Component

// TODO: Use correct name including prefix
fun Player.realName(): Component {
    return Component.text(this.username, COLOR_VARIABLE)
}