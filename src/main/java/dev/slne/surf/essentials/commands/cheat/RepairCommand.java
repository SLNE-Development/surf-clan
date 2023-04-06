package dev.slne.surf.essentials.commands.cheat;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import dev.slne.surf.essentials.SurfEssentials;
import dev.slne.surf.essentials.utils.EssentialsUtil;
import dev.slne.surf.essentials.utils.color.Colors;
import dev.slne.surf.essentials.utils.permission.Permissions;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class RepairCommand {

    public static void register(){
        SurfEssentials.registerPluginBrigadierCommand("repair", RepairCommand::literal);
    }

    private static void literal(LiteralArgumentBuilder<CommandSourceStack> literal){
        literal.requires(sourceStack -> sourceStack.hasPermission(2, Permissions.REPAIR_SELF_PERMISSION));

        literal.executes(context -> repair(context.getSource(), context.getSource().getPlayerOrException()));
        literal.then(Commands.argument("player", EntityArgument.player())
                .requires(sourceStack -> sourceStack.hasPermission(2, Permissions.REPAIR_OTHER_PERMISSION))
                .executes(context -> repair(context.getSource(), EntityArgument.getPlayer(context, "players"))));
    }

    private static int repair(CommandSourceStack source, ServerPlayer targetUnchecked) throws CommandSyntaxException{
        ServerPlayer target = EssentialsUtil.checkSinglePlayerSuggestion(source, targetUnchecked);
        ItemStack item = target.getMainHandItem();

        if (!item.isDamageableItem()){
            if (source.isPlayer()){
                EssentialsUtil.sendError(source, Component.text("Das Item ", Colors.ERROR)
                        .append(PaperAdventure.asAdventure(item.getDisplayName()))
                        .append(Component.text(" kann nicht repariert werden!", Colors.ERROR)));

            }else throw  ERROR_NOT_DAMAGEABLE.create(item);
            return 0;
        }

        item.setDamageValue(0);

        if (source.isPlayer()){
            EssentialsUtil.sendSuccess(source, PaperAdventure.asAdventure(item.getDisplayName())
                    .append(Component.text(" von ", Colors.SUCCESS))
                    .append(target.adventure$displayName.colorIfAbsent(Colors.TERTIARY))
                    .append(Component.text(" wurde repariert!", Colors.SUCCESS)));
        }else {
            source.sendSuccess(net.minecraft.network.chat.Component.literal("The item ")
                    .append(item.getDisplayName())
                    .append(net.minecraft.network.chat.Component.literal(" from "))
                    .append(target.getDisplayName())
                    .append(net.minecraft.network.chat.Component.literal(" was repaired")), false);
        }

        return 1;
    }

    private static final DynamicCommandExceptionType ERROR_NOT_DAMAGEABLE = new DynamicCommandExceptionType(item -> ((ItemStack) item).getDisplayName()
            .copy().append(net.minecraft.network.chat.Component.literal(" is not damageable!")
                    .withStyle(ChatFormatting.RED)));

}
