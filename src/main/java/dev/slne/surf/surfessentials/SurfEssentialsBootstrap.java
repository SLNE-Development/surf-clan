package dev.slne.surf.surfessentials;

import dev.slne.surf.surfessentials.commands.EssentialsCommandRegistry;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class SurfEssentialsBootstrap implements PluginBootstrap {

  @Override
  public void bootstrap(@NotNull BootstrapContext bootstrapContext) {
    bootstrapContext.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS.newHandler(
        event -> EssentialsCommandRegistry.registerCommands(bootstrapContext.getPluginMeta(),
            event.registrar())));
  }

  @Override
  public @NotNull JavaPlugin createPlugin(@NotNull PluginProviderContext context) {
    return new SurfEssentials();
  }
}
