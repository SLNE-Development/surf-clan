package dev.slne.surf.essentials.brigadier;

import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.slne.surf.essentials.SurfEssentials;
import dev.slne.surf.essentials.brigadier.builder.tp.TeleportAllBuilder;
import me.lucko.commodore.Commodore;

public class TpTabComplete {
    SurfEssentials surf = SurfEssentials.getInstance();

    public void register(Commodore commodore){
        //TeleportAll command completions
        builder(commodore, "tpall", new TeleportAllBuilder().teleportAllBuilder());
    }



    private void builder(Commodore commodore, String command, LiteralCommandNode commandNode){
        commodore.register(surf.getCommand(command), commandNode);
    }
}
