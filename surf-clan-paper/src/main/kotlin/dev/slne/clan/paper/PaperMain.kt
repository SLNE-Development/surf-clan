package dev.slne.clan.paper

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.api.paper.extensions.pluginManager
import dev.slne.surf.clan.core.ClanInstance
import org.bukkit.plugin.java.JavaPlugin

class PaperMain : SuspendingJavaPlugin() {
    override suspend fun onLoadAsync() {
        ClanInstance.load()
    }

    override suspend fun onEnableAsync() {
        ClanInstance.enable()
    }

    override suspend fun onDisableAsync() {
        ClanInstance.disable()
    }

    fun checkSurfChat() = pluginManager.isPluginEnabled("surf-chat-paper")
}

val plugin get() = JavaPlugin.getPlugin(PaperMain::class.java)