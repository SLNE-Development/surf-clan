package dev.slne.surf.surfessentials.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.configuration.PluginMeta;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.util.Arrays;

public final class EssentialsCommandRegistry {
  private static final ObjectList<EssentialsCommand> COMMANDS;

  static {
    COMMANDS = new ObjectArrayList<>();
  }

  private static void register(EssentialsCommand command) {
    COMMANDS.add(command);
  }

  @SuppressWarnings("UnstableApiUsage")
  public static void registerCommands(PluginMeta pluginMeta, Commands commands) {
    for (EssentialsCommand command : COMMANDS) {
      final LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal(command.getLabel());
      command.build(root);

      commands.register(pluginMeta, root.build(), command.getDescription(), Arrays.stream(command.getAliases()).toList());
    }
  }
}
