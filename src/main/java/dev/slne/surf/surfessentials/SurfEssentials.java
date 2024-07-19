package dev.slne.surf.surfessentials;

import com.github.retrooper.packetevents.PacketEvents;
import de.tr7zw.changeme.nbtapi.NBTContainer;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.slne.surf.surfessentials.message.MessageLoader;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class SurfEssentials extends JavaPlugin {

  @Override
  public void onLoad() {
    PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
    PacketEvents.getAPI().load();
    CommandAPI.onLoad(new CommandAPIBukkitConfig(this)
        .shouldHookPaperReload(true)
        .initializeNBTAPI(NBTContainer.class, NBTContainer::new));

    MessageLoader.INSTANCE.load();
  }

  @Override
  public void onEnable() {
    PacketEvents.getAPI().init();
    CommandAPI.onEnable();
  }

  @Override
  public void onDisable() {
    PacketEvents.getAPI().terminate();
    CommandAPI.onDisable();
  }


  public static @NotNull SurfEssentials getInstance() {
    return getPlugin(SurfEssentials.class);
  }
}
