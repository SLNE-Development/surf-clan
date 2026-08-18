package dev.slne.clan.minestom.command

import com.google.inject.Inject
import dev.slne.minestom.lobby.api.command.CommandRegistrar

/**
 * Registers the clan commands of this plugin.
 */
class ClanCommandRegistrar @Inject constructor() : CommandRegistrar {
    override fun register() {
        clanCommand()

        clanChatCommand("clanchat").register()
        clanChatCommand("cc").register()
    }
}
