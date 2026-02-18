package dev.slne.clan.paper

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.clan.core.ClanInstance
import org.bukkit.plugin.java.JavaPlugin

class PaperMain : SuspendingJavaPlugin() {
    override suspend fun onLoadAsync() {
        ClanInstance.get().load()
    }

    override suspend fun onEnableAsync() {
        ClanInstance.get().enable()
    }

    override suspend fun onDisableAsync() {
        ClanInstance.get().disable()
    }
}

val plugin get() = JavaPlugin.getPlugin(PaperMain::class.java)