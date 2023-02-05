package dev.slne.surf.essentials.main.commands.minecraft;

import aetherial.spigot.plugin.annotation.permission.PermissionTag;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.slne.surf.api.utils.message.SurfColors;
import dev.slne.surf.essentials.main.utils.EssentialsUtil;
import dev.slne.surf.essentials.main.utils.Permissions;
import dev.slne.surf.essentials.main.utils.brigadier.BrigadierCommand;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.GameType;

import javax.annotation.Nullable;
import java.util.Collections;

@PermissionTag(name = Permissions.SPECTATE_SELF_PERMISSION, desc = "Allows you to spectate other players")
@PermissionTag(name = Permissions.SPECTATE_OTHER_PERMISSION, desc = "Allows you to spectate other players from other players")
public class SpectateCommand extends BrigadierCommand {
    @Override
    public String[] names() {
        return new String[]{"spectate"};
    }

    @Override
    public String usage() {
        return "/spectate <target> [<player>]";
    }

    @Override
    public String description() {
        return "Spectate other players";
    }

    @Override
    public void literal(LiteralArgumentBuilder<CommandSourceStack> literal) {
        literal.requires(sourceStack -> sourceStack.hasPermission(2, Permissions.SPECTATE_SELF_PERMISSION));

        literal.executes(context -> spectate(context.getSource(), null, context.getSource().getPlayerOrException()));

        literal.then(Commands.argument("target", EntityArgument.entity())
                .executes(context -> spectate(context.getSource(), EntityArgument.getEntity(context, "target"), context.getSource().getPlayerOrException()))
                .then(Commands.argument("player", EntityArgument.player())
                        .requires(sourceStack -> sourceStack.hasPermission(2, Permissions.SPECTATE_OTHER_PERMISSION))
                        .executes(context -> spectate(context.getSource(), EntityArgument.getEntity(context, "target"), EntityArgument.getPlayer(context, "player")))));
    }

    private int spectate(CommandSourceStack source, @Nullable Entity entityUnchecked, ServerPlayer playerUnchecked) throws CommandSyntaxException {
        Entity entity = EssentialsUtil.checkEntitySuggestion(source, Collections.singleton(entityUnchecked)).iterator().next();
        ServerPlayer player = EssentialsUtil.checkPlayerSuggestion(source, Collections.singleton(playerUnchecked)).iterator().next();

        if (player == entity) throw ERROR_SELF.create();
        if (player.gameMode.getGameModeForPlayer() != GameType.SPECTATOR){
            player.setGameMode(GameType.SPECTATOR);
        }

        player.setCamera(entity);

        if (source.isPlayer()){
            if (entity != null){
                EssentialsUtil.sendSuccess(source, player.adventure$displayName.colorIfAbsent(SurfColors.TERTIARY)
                        .append(Component.text(" beobachtet nun ", SurfColors.SUCCESS))
                        .append(getEntityDisplayName(entity))
                        .append(Component.text(".", SurfColors.SUCCESS)));
            }else {
                EssentialsUtil.sendSuccess(source, player.adventure$displayName.colorIfAbsent(SurfColors.TERTIARY)
                        .append(Component.text(" beobachtet nun niemanden mehr.", SurfColors.SUCCESS)));
            }
        }else {
            if (entity != null) {
                source.sendSuccess(net.minecraft.network.chat.Component.translatable("commands.spectate.success.started", entity.getDisplayName()), false);
            } else {
                source.sendSuccess(net.minecraft.network.chat.Component.translatable("commands.spectate.success.stopped"), false);
            }
        }
        return 1;
    }

    private Component getEntityDisplayName(Entity entity){
        if (entity instanceof ServerPlayer player){
            return player.adventure$displayName.colorIfAbsent(SurfColors.TERTIARY);
        }else {
            return PaperAdventure.asAdventure(entity.getDisplayName());
        }
    }

    private static final SimpleCommandExceptionType ERROR_SELF = new SimpleCommandExceptionType(net.minecraft.network.chat.Component.translatable("commands.spectate.self"));
}
