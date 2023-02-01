package dev.slne.surf.essentials.main.commands.cheat.gui;

import aetherial.spigot.plugin.annotation.permission.PermissionTag;
import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.slne.surf.api.SurfApi;
import dev.slne.surf.api.utils.message.SurfColors;
import dev.slne.surf.essentials.SurfEssentials;
import dev.slne.surf.essentials.main.utils.Permissions;
import net.kyori.adventure.text.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.player.Player;
import org.bukkit.Bukkit;

import java.util.Collection;

@PermissionTag(name = Permissions.SMITHING_TABLE_SELF_PERMISSION, desc = "Allows you to open the smithingtable gui for yourself")
@PermissionTag(name = Permissions.SMITHING_TABLE_OTHER_PERMISSION, desc = "Allows you to open the smithingtable gui for others")
public class SmithingTableCommand {
    public static void register(){
        SurfEssentials.registerPluginBrigadierCommand("smithingtable", SmithingTableCommand::literal).setUsage("/smithingtable [<targets>]")
                .setDescription("Opens the smithingtable gui for the targets");
        SurfEssentials.registerPluginBrigadierCommand("smithing", SmithingTableCommand::literal).setUsage("/smithingtable [<targets>]")
                .setDescription("Opens the smithingtable gui for the targets");
    }

    private static void literal(LiteralArgumentBuilder<CommandSourceStack> literal){
        literal.requires(sourceStack -> sourceStack.hasPermission(2, Permissions.SMITHING_TABLE_SELF_PERMISSION));
        literal.executes(context -> open(context.getSource(), ImmutableList.of(context.getSource().getPlayerOrException())));
        literal.then(Commands.argument("targets", EntityArgument.players())
                .requires(sourceStack -> sourceStack.hasPermission(2, Permissions.SMITHING_TABLE_OTHER_PERMISSION))
                .executes(context -> open(context.getSource(), EntityArgument.getPlayers(context, "targets"))));
    }

    private static int open(CommandSourceStack source, Collection<? extends Player> targets) throws CommandSyntaxException {
        for (Player target : targets) {
            Bukkit.getPlayer(target.getUUID()).openSmithingTable(target.getBukkitEntity().getLocation(), true);
        }
        if (source.isPlayer()){
            if (targets.size() == 1){
                SurfApi.getUser(source.getPlayerOrException().getUUID()).thenAcceptAsync(user -> user.sendMessage(SurfApi.getPrefix()
                        .append(Component.text("Der Schmiedetisch wurde für ", SurfColors.SUCCESS))
                        .append(Bukkit.getPlayer(targets.iterator().next().getUUID()).displayName().colorIfAbsent(SurfColors.YELLOW))
                        .append(Component.text(" geöffnet", SurfColors.SUCCESS))));
            }else {
                SurfApi.getUser(source.getPlayerOrException().getUUID()).thenAcceptAsync(user -> user.sendMessage(SurfApi.getPrefix()
                        .append(Component.text("Der Schmiedetisch wurde für ", SurfColors.SUCCESS))
                        .append(Component.text(targets.size(), SurfColors.TERTIARY))
                        .append(Component.text(" Spieler geöffnet", SurfColors.SUCCESS))));
            }
        }else {
            if (targets.size() == 1){
                source.sendSuccess(net.minecraft.network.chat.Component.literal("Opened smithing table for ")
                        .withStyle(ChatFormatting.WHITE)
                        .append(targets.iterator().next().getDisplayName()), false);
            }else{
                source.sendSuccess(net.minecraft.network.chat.Component.literal("Opened smithing table for ")
                        .withStyle(ChatFormatting.WHITE)
                        .append(net.minecraft.network.chat.Component.literal(String.valueOf(targets.size()))
                                .withStyle(ChatFormatting.GOLD))
                        .append(net.minecraft.network.chat.Component.literal(" players")
                                .withStyle(ChatFormatting.WHITE)), false);
            }
        }
        return 1;
    }
}
