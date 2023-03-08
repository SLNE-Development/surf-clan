package dev.slne.surf.essentials.commands.cheat;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import dev.slne.surf.essentials.utils.EssentialsUtil;
import dev.slne.surf.essentials.utils.brigadier.BrigadierCommand;
import dev.slne.surf.essentials.utils.color.Colors;
import dev.slne.surf.essentials.utils.permission.Permissions;
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
import net.minecraft.world.item.Items;

public class UnhatCommand extends BrigadierCommand {
    @Override
    public String[] names() {
        return new String[]{"unhat"};
    }

    @Override
    public String usage() {
        return "/unhat";
    }

    @Override
    public String description() {
        return "Puts the item on your head in your inventory";
    }

    @Override
    public void literal(LiteralArgumentBuilder<CommandSourceStack> literal) {
        literal.requires(sourceStack -> sourceStack.hasPermission(2, Permissions.UNHAT_SELF_PERMISSION));
        literal.executes(context -> hat(context.getSource(), context.getSource().getPlayerOrException()));

        literal.then(Commands.argument("player", EntityArgument.player())
                .requires(sourceStack -> sourceStack.hasPermission(2, Permissions.UNHAT_OTHER_PERMISSION))
                .executes(context -> hat(context.getSource(), EntityArgument.getPlayer(context, "player"))));
    }

    private int hat(CommandSourceStack source, ServerPlayer playerUnchecked) throws CommandSyntaxException{
        ServerPlayer player = EssentialsUtil.checkSinglePlayerSuggestion(source, playerUnchecked);
        Inventory playerInventory = player.getInventory();
        ItemStack itemStackOnHead = playerInventory.getArmor(EquipmentSlot.HEAD.getIndex());
        int freeSlot = playerInventory.getFreeSlot();

        if (freeSlot == -1) throw NO_SPACE_IN_INVENTORY.create(player);

        playerInventory.setItem(freeSlot, itemStackOnHead);
        playerInventory.setItem(playerInventory.getContainerSize() - 2, Items.AIR.getDefaultInstance());

        if (source.isPlayer()) {
            EssentialsUtil.sendSuccess(source, player.adventure$displayName.colorIfAbsent(Colors.TERTIARY)
                    .append(Component.text(" hat das Item ", Colors.SUCCESS)
                            .append(PaperAdventure.asAdventure(itemStackOnHead.getDisplayName()).colorIfAbsent(Colors.TERTIARY))
                            .append(Component.text(" abgesetzt.", Colors.SUCCESS))));
        }else {
            source.sendSuccess(player.getDisplayName()
                    .copy().append(net.minecraft.network.chat.Component.literal(" has unput the item ")
                            .withStyle(ChatFormatting.GREEN)
                            .append(itemStackOnHead.getDisplayName())), false);
        }

        return 1;
    }

    private static final DynamicCommandExceptionType NO_SPACE_IN_INVENTORY = new DynamicCommandExceptionType(player -> PaperAdventure.asVanilla(((ServerPlayer) player).adventure$displayName.colorIfAbsent(Colors.TERTIARY))
            .copy().append(net.minecraft.network.chat.Component.literal(" has no free inventory space!")
                    .withStyle(ChatFormatting.RED)));
}
