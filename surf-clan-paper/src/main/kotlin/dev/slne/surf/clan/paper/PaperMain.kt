package dev.slne.surf.clan.paper

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.clan.paper.commands.clanCommand
import org.bukkit.plugin.java.JavaPlugin

class PaperMain : SuspendingJavaPlugin() {
    override suspend fun onLoadAsync() {

    }

    override suspend fun onEnableAsync() {
        clanCommand()
    }

    override suspend fun onDisableAsync() {

    }
}

val plugin get() = JavaPlugin.getPlugin(PaperMain::class.java)