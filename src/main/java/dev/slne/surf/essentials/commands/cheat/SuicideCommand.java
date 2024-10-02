package dev.slne.surf.essentials.commands.cheat;

import dev.slne.surf.essentials.SurfEssentials;
import dev.slne.surf.essentials.commands.EssentialsCommand;
import dev.slne.surf.essentials.utils.EssentialsUtil;
import dev.slne.surf.essentials.utils.permission.Permissions;
import lombok.val;
import org.bukkit.entity.LivingEntity;

public class SuicideCommand extends EssentialsCommand { // TODO: broken
    public SuicideCommand() {
        super("suicide", "suicide", "Lets you commit suicide");

        withPermission(Permissions.SUICIDE_PERMISSION);

        executesNative((sender, args) -> {
            val target = getSpecialEntityOrException(sender, LivingEntity.class);

            EssentialsUtil.damage(target, Float.MAX_VALUE, target);
            EssentialsUtil.sendSuccess(target, "Du hast Selbstmord begangen");

            return 1;
        });
    }
}
