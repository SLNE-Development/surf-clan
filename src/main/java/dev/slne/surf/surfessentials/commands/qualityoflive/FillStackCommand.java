package dev.slne.surf.surfessentials.commands.qualityoflive;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.slne.surf.surfessentials.commands.EssentialsCommand;
import dev.slne.surf.surfessentials.commands.Permissions;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;

public class FillStackCommand extends EssentialsCommand {

  public FillStackCommand() {
    super("fillstack", "Fills the item in the player's hand to the maximum stack size.", "more");
  }

  @Override
  public void build(LiteralArgumentBuilder<CommandSourceStack> root) {
    withPermission(root, Permissions.FILL_STACK_PERMISSION);

    root.executes(commandContext -> commandContext.getSource())
        .then(Commands.argument("player", ArgumentTypes.player())
            .executes(commandContext -> ));
  }

  private int fillStack() {
    return Command.SINGLE_SUCCESS;
  }
}
