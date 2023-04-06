package dev.slne.surf.essentials.commands.cheat.gui;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.slne.surf.essentials.utils.EssentialsUtil;
import dev.slne.surf.essentials.utils.color.Colors;
import dev.slne.surf.essentials.utils.nms.brigadier.BrigadierCommand;
import dev.slne.surf.essentials.utils.permission.Permissions;
import net.kyori.adventure.text.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;
import java.util.List;

public class CartographyTableCommand extends BrigadierCommand {
    @Override
    public String[] names() {
        return new String[]{"cartographytable"};
    }

    @Override
    public String usage() {
        return "/cartographytable [<targets>]";
    }

    @Override
    public String description() {
        return "Opens the cartographytable gui for the targets";
    }

    @Override
    public void literal(LiteralArgumentBuilder<CommandSourceStack> literal){
        literal.requires(EssentialsUtil.checkPermissions(Permissions.CARTOGRAPHY_TABLE_SELF_PERMISSION, Permissions.CARTOGRAPHY_TABLE_OTHER_PERMISSION));
        literal.executes(context -> open(context.getSource(), List.of(context.getSource().getPlayerOrException())));

        literal.then(Commands.argument("targets", EntityArgument.players())
                .requires(EssentialsUtil.checkPermissions(Permissions.CARTOGRAPHY_TABLE_OTHER_PERMISSION))
                .executes(context -> open(context.getSource(), EntityArgument.getPlayers(context, "targets"))));
    }

    private static int open(CommandSourceStack source, Collection<ServerPlayer> targetsUnchecked) throws CommandSyntaxException {
        Collection<ServerPlayer> targets = EssentialsUtil.checkPlayerSuggestion(source, targetsUnchecked);
        for (Player target : targets) {
            target.getBukkitEntity().openCartographyTable(target.getBukkitEntity().getLocation(), true);
        }
        if (source.isPlayer()){
            if (targets.size() == 1){
                EssentialsUtil.sendSuccess(source, Component.text("Der Karten Tisch wurde für ", Colors.SUCCESS)
                        .append(targets.iterator().next().adventure$displayName.colorIfAbsent(Colors.TERTIARY))
                        .append(Component.text(" geöffnet", Colors.SUCCESS)));
            }else {
                EssentialsUtil.sendSuccess(source, Component.text("Der Karten Tisch wurde für ", Colors.SUCCESS)
                        .append(Component.text(targets.size(), Colors.TERTIARY))
                        .append(Component.text(" Spieler geöffnet", Colors.SUCCESS)));
            }
        }else {
            if (targets.size() == 1){
                source.sendSuccess(net.minecraft.network.chat.Component.literal("Opened cartography table for ")
                        .withStyle(ChatFormatting.WHITE)
                        .append(targets.iterator().next().getDisplayName()), false);
            }else{
                source.sendSuccess(net.minecraft.network.chat.Component.literal("Opened cartography table table for ")
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
