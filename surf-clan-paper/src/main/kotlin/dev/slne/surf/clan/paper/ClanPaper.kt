package dev.slne.surf.clan.paper

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import org.bukkit.plugin.java.JavaPlugin

val plugin get() = JavaPlugin.getPlugin(ClanPaper::class.java)

class ClanPaper : SuspendingJavaPlugin() {
}