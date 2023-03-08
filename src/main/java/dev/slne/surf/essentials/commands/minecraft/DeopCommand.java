package dev.slne.surf.essentials.commands.minecraft;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.slne.surf.essentials.SurfEssentials;
import dev.slne.surf.essentials.utils.EssentialsUtil;
import dev.slne.surf.essentials.utils.color.Colors;
import dev.slne.surf.essentials.utils.permission.Permissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.players.PlayerList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;

public class DeopCommand {
    public static void register(){
        SurfEssentials.registerPluginBrigadierCommand("deop", DeopCommand::literal).setUsage("/deop <player>")
                .setDescription("Deop a player");
    }

    private static void literal(LiteralArgumentBuilder<CommandSourceStack> literal){
        literal.requires(sourceStack -> sourceStack.hasPermission(2, Permissions.DEOP_PERMISSION));

        literal.then(Commands.argument("players", GameProfileArgument.gameProfile())
                .suggests((context, builder) -> SharedSuggestionProvider.suggest(context.getSource().getServer().getPlayerList().getOpNames(), builder))
                .executes(context -> deop(context.getSource(), GameProfileArgument.getGameProfiles(context, "players"))));
    }

    private static int deop(CommandSourceStack source, Collection<GameProfile> players)throws CommandSyntaxException {
        PlayerList playerList = source.getServer().getPlayerList();
        int i = 0;

        for (GameProfile gameProfile : players) {
            if (playerList.isOp(gameProfile)) {
                playerList.deop(gameProfile);
                ++i;
                if (source.isPlayer()){
                    Player player = Bukkit.getPlayer(gameProfile.getId());
                    net.kyori.adventure.text.Component displayName = (player == null) ? net.kyori.adventure.text.Component.text(gameProfile.getName()) : player.displayName();

                    Bukkit.broadcast(EssentialsUtil.getPrefix()
                            .append(displayName.colorIfAbsent(Colors.TERTIARY))
                            .append(net.kyori.adventure.text.Component.text(" ist durch ", Colors.INFO))
                            .append(source.getPlayerOrException().adventure$displayName.colorIfAbsent(Colors.TERTIARY))
                            .append(net.kyori.adventure.text.Component.text(" kein Operator mehr!", Colors.INFO)), "surf.essentials.announce.op");
                }else {
                    source.sendSuccess(Component.translatable("commands.op.success", gameProfile.getName()), true);
                }
            }
        }

        if (i == 0) {
            if (source.isPlayer()){
                EssentialsUtil.sendError(source, "Es hat sich nicht geändert! Die Spieler sind keine Operatoren");
            }else {
                throw ERROR_NOT_OP.create();
            }
        }
        return i;
    }

    private static final SimpleCommandExceptionType ERROR_NOT_OP = new SimpleCommandExceptionType(Component.translatable("commands.deop.failed"));
}
