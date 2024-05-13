package dev.slne.surf.surfessentials;

import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class SurfEssentials extends JavaPlugin {

  @Override
  public void onLoad() {
    PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
    PacketEvents.getAPI().load();
  }

  @Override
  public void onEnable() {
    PacketEvents.getAPI().init();
  }

  @Override
  public void onDisable() {

    PacketEvents.getAPI().terminate();
  }


  public static @NotNull SurfEssentials getInstance() {
    return getPlugin(SurfEssentials.class);
  }
}
