package dev.slne.surf.essentials.commands.cheat;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.slne.surf.essentials.utils.EssentialsUtil;
import dev.slne.surf.essentials.utils.color.Colors;
import dev.slne.surf.essentials.utils.nms.brigadier.BrigadierCommand;
import dev.slne.surf.essentials.utils.permission.Permissions;
import net.kyori.adventure.text.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import java.util.Collection;
import java.util.Collections;

public class HealCommand extends BrigadierCommand {
    @Override
    public String[] names() {
        return new String[]{"heal"};
    }

    @Override
    public String usage() {
        return "/heal [<targets>]";
    }

    @Override
    public String description() {
        return "Heals the targets";
    }

    public void literal(LiteralArgumentBuilder<CommandSourceStack> literal) {
        literal.requires(EssentialsUtil.checkPermissions(Permissions.HEAL_SELF_PERMISSION, Permissions.HEAL_OTHER_PERMISSION));

        literal.executes(context -> heal(context.getSource(), Collections.singleton(context.getSource().getPlayerOrException())));
        literal.then(Commands.argument("players", EntityArgument.players())
                        .requires(EssentialsUtil.checkPermissions(Permissions.HEAL_OTHER_PERMISSION))
                        .executes(context -> heal(context.getSource(), EntityArgument.getPlayers(context, "players"))));
    }

    private int heal(CommandSourceStack source, Collection<ServerPlayer> targetsUnchecked) throws CommandSyntaxException {
        final var targets = EssentialsUtil.checkPlayerSuggestion(source, targetsUnchecked);
        int successfulChanges = 0;

        for (ServerPlayer target : targets) {
            target.heal(target.getMaxHealth(), EntityRegainHealthEvent.RegainReason.REGEN, true);
            successfulChanges ++;
            EssentialsUtil.sendSuccess(target, (Component.text("Du wurdest geheilt! ", Colors.GREEN)));
        }

        final var target = targets.iterator().next();
        if (source.isPlayer()){
            if (successfulChanges == 1 && source.getPlayerOrException() != targets.iterator().next()){
                EssentialsUtil.sendSuccess(source, EssentialsUtil.getDisplayName(target)
                        .append(Component.text(" wurde geheilt!", Colors.SUCCESS)));
            }else if (successfulChanges >= 1 && source.getPlayerOrException() != targets.iterator().next()){
                EssentialsUtil.sendSuccess(source, Component.text(successfulChanges, Colors.TERTIARY)
                        .append(Component.text(" Spieler wurden geheilt!", Colors.SUCCESS)));
            }
        }else {
            if (successfulChanges == 1){
                source.sendSuccess(target.getDisplayName()
                        .copy().append(net.minecraft.network.chat.Component.literal(" was healed")), false);
            }else {
                source.sendSuccess(net.minecraft.network.chat.Component.literal(successfulChanges + " players were healed"), false);
            }
        }
        return successfulChanges;
    }
}
