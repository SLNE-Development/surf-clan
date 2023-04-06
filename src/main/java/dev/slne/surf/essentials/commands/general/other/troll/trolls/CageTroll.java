package dev.slne.surf.essentials.commands.general.other.troll.trolls;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.slne.surf.essentials.SurfEssentials;
import dev.slne.surf.essentials.utils.EssentialsUtil;
import dev.slne.surf.essentials.utils.abtract.CraftUtil;
import dev.slne.surf.essentials.utils.color.Colors;
import net.kyori.adventure.text.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;
import java.util.UUID;

public class CageTroll {
    public static ArrayList<UUID> playersInCage = new ArrayList<>();

    public static RequiredArgumentBuilder<CommandSourceStack, EntitySelector> cage(LiteralArgumentBuilder<CommandSourceStack> literal){
        return Commands.argument("player", EntityArgument.player())
                .executes(context -> executeCage(context, EntityArgument.getPlayer(context, "player").getBukkitEntity(), 60, false))
                .then(Commands.argument("time", IntegerArgumentType.integer(1, 6400))
                        .executes(context -> executeCage(context, EntityArgument.getPlayer(context, "player").getBukkitEntity(),
                                IntegerArgumentType.getInteger(context, "time"), false))
                        .then(Commands.argument("force", BoolArgumentType.bool())
                                .executes(context -> executeCage(context, EntityArgument.getPlayer(context, "player").getBukkitEntity(),
                                        IntegerArgumentType.getInteger(context, "time"), BoolArgumentType.getBool(context, "force")))));
    }

    private static int executeCage(CommandContext<CommandSourceStack> context, Player target, int timeInSeconds, boolean force) throws CommandSyntaxException {
        EssentialsUtil.checkPlayerSuggestion(context.getSource(), CraftUtil.toServerPlayer(target));
        CommandSourceStack source = context.getSource();

        if (isPlayerInCage(target.getUniqueId())){
            cancelCageTroll(target.getUniqueId());

            if (source.isPlayer()){
                EssentialsUtil.sendSuccess(source, target.displayName().colorIfAbsent(Colors.YELLOW)
                        .append(Component.text(" wurde befreit!", Colors.SUCCESS)));
            }else{
                source.sendSuccess(EntityArgument.getPlayer(context, "player").getDisplayName()
                        .copy().append(net.minecraft.network.chat.Component.literal(" has been freed!")
                                .withStyle(ChatFormatting.GREEN)), false);
            }
            return 1;
        }

        Material cageMaterial = Material.GLASS;
        Location entityLocation = target.getLocation();

        int sideLength = 3;
        int height = 3;

        int delta = sideLength / 2;
        Location corner1 = new Location(entityLocation.getWorld(), entityLocation.getBlockX() + delta, entityLocation.getBlockY() + 1,
                entityLocation.getBlockZ() - delta);
        Location corner2 = new Location(entityLocation.getWorld(), entityLocation.getBlockX() - delta, entityLocation.getBlockY() + 1,
                entityLocation.getBlockZ() + delta);
        int minX = Math.min(corner1.getBlockX(), corner2.getBlockX());
        int maxX = Math.max(corner1.getBlockX(), corner2.getBlockX());
        int minZ = Math.min(corner1.getBlockZ(), corner2.getBlockZ());
        int maxZ = Math.max(corner1.getBlockZ(), corner2.getBlockZ());

        playersInCage.add(target.getUniqueId());

        for(int x = minX; x <= maxX; ++x) {
            for(int y = 0; y < height; ++y) {
                for(int z = minZ; z <= maxZ; ++z) {
                    Block block;
                    if (x == minX || x == maxX || z == minZ || z == maxZ) {
                        block = corner1.getWorld().getBlockAt(x, entityLocation.getBlockY() + y, z);
                        if (block.getType() != Material.AIR && !force) continue;
                        block.setType(cageMaterial);
                    }

                    if (y == height - 1) {
                        block = corner1.getWorld().getBlockAt(x, entityLocation.getBlockY() + y + 1, z);
                        if (block.getType() != Material.AIR && !force) continue;
                        block.setType(cageMaterial);
                    }
                }
            }
        }

        double middleX = Math.round( (minX + maxX) / 2.0 * 2) / 2.0 + 0.5;
        double middleY = Math.round( entityLocation.getY() * 2) / 2.0;
        double middleZ = Math.round( (minZ + maxZ) / 2.0 * 2) / 2.0 + 0.5;

        Location middle = new Location(entityLocation.getWorld(), middleX, middleY, middleZ, entityLocation.getYaw(), entityLocation.getPitch());

        target.teleportAsync(middle, PlayerTeleportEvent.TeleportCause.COMMAND);

        Bukkit.getScheduler().runTaskLaterAsynchronously(SurfEssentials.getInstance(), () -> cancelCageTroll(target.getUniqueId()), 20L * timeInSeconds);

        //success message
        if (source.isPlayer()){
            EssentialsUtil.sendSuccess(source, target.displayName().colorIfAbsent(Colors.YELLOW)
                    .append(Component.text(" sitzt jetzt in der Falle!", Colors.SUCCESS)));
        }else{
            source.sendSuccess(EntityArgument.getPlayer(context, "player").getDisplayName()
                    .copy().append(net.minecraft.network.chat.Component.literal(" is now trapped!")
                            .withStyle(ChatFormatting.GREEN)), false);
        }
        return 1;
    }

    public static void cancelCageTroll(){
        playersInCage.forEach(uuid -> playersInCage.remove(uuid));
    }

    public static void cancelCageTroll(UUID uuid){
        playersInCage.remove(uuid);
    }

    public static Boolean isPlayerInCage(UUID uuid){
        return playersInCage.contains(uuid);
    }
}
