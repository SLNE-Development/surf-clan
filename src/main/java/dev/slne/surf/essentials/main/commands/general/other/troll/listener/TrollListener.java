package dev.slne.surf.essentials.main.commands.general.other.troll.listener;

import dev.slne.surf.essentials.SurfEssentials;
import dev.slne.surf.essentials.main.commands.general.other.troll.trolls.AnvilTroll;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class TrollListener {
    public static void register(){
        Plugin plugin = SurfEssentials.getInstance();
        PluginManager pluginManager = plugin.getServer().getPluginManager();

        // Water troll Listener
        pluginManager.registerEvents(new WaterTrollListener(), plugin);

        //Mlg troll Listener
        pluginManager.registerEvents(new MlgTrollListener(), plugin);

        //Cage troll Listener
        pluginManager.registerEvents(new CageTrollListener(), plugin);

        //anvil troll Listener
        pluginManager.registerEvents(new AnvilTroll(), plugin);
    }
}
