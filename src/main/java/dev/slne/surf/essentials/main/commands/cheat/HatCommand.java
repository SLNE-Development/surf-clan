package dev.slne.surf.essentials.main.commands.cheat;

import aetherial.spigot.plugin.annotation.permission.PermissionTag;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.slne.surf.api.utils.message.SurfColors;
import dev.slne.surf.essentials.main.utils.EssentialsUtil;
import dev.slne.surf.essentials.main.utils.Permissions;
import dev.slne.surf.essentials.main.utils.brigadier.BrigadierCommand;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

@PermissionTag(name = Permissions.HAT_SELF_PERMISSION, desc = "Allows you to put the item in your main hand on your head")
@PermissionTag(name = Permissions.HAT_OTHER_PERMISSION, desc = "Allows you to put the item in the other player's main hand on their head")
public class HatCommand extends BrigadierCommand {
    @Override
    public String[] names() {
        return new String[]{"hat"};
    }

    @Override
    public String usage() {
        return "/hat";
    }

    @Override
    public String description() {
        return "Puts the item in your main hand on your head";
    }

    @Override
    public void literal(LiteralArgumentBuilder<CommandSourceStack> literal) {
        literal.requires(sourceStack -> sourceStack.hasPermission(2, Permissions.HAT_SELF_PERMISSION));
        literal.executes(context -> hat(context.getSource(), context.getSource().getPlayerOrException()));

        literal.then(Commands.argument("player", EntityArgument.player())
                .requires(sourceStack -> sourceStack.hasPermission(2, Permissions.HAT_OTHER_PERMISSION))
                .executes(context -> hat(context.getSource(), EntityArgument.getPlayer(context, "player"))));
    }

    private int hat(CommandSourceStack source, ServerPlayer player) throws CommandSyntaxException {
        Inventory playerInventory = player.getInventory();
        ItemStack itemStackInMainHand = player.getMainHandItem();
        ItemStack itemStackOnHead = playerInventory.getArmor(EquipmentSlot.HEAD.getIndex());

        playerInventory.setItem(playerInventory.selected, itemStackOnHead);
        playerInventory.setItem(playerInventory.getContainerSize() - 2, itemStackInMainHand);

        if (source.isPlayer()) {
            EssentialsUtil.sendSuccess(source, player.adventure$displayName.colorIfAbsent(SurfColors.TERTIARY)
                    .append(Component.text(" hat das Item ", SurfColors.SUCCESS)
                            .append(PaperAdventure.asAdventure(itemStackInMainHand.getDisplayName()).colorIfAbsent(SurfColors.TERTIARY))
                            .append(Component.text(" aufgesetzt bekommen.", SurfColors.SUCCESS))));
        }else {
            source.sendSuccess(player.getDisplayName()
                    .copy().append(net.minecraft.network.chat.Component.literal(" has put on the item ")
                            .withStyle(ChatFormatting.GREEN)
                            .append(itemStackInMainHand.getDisplayName())), false);
        }

        return 1;
    }
}
