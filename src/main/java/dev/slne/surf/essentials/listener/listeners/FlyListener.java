package dev.slne.surf.essentials.listener.listeners;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import dev.slne.surf.essentials.SurfEssentials;
import dev.slne.surf.essentials.commands.cheat.FlyCommand;
import dev.slne.surf.essentials.utils.EssentialsUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.persistence.PersistentDataType;

public class FlyListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        EssentialsUtil.runOnEntity(event.getPlayer(), () -> updateFlyMode(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
        EssentialsUtil.runOnEntityDelayed(event.getPlayer(), 5L, () -> updateFlyMode(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerRespawn(PlayerPostRespawnEvent event) {
        EssentialsUtil.runOnEntity(event.getPlayer(), () -> updateFlyMode(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        EssentialsUtil.runOnEntityDelayed(event.getPlayer(), 5L, () -> updateFlyMode(event.getPlayer()));
    }

    private void updateFlyMode(Player player) {
        if (player.getPersistentDataContainer().getOrDefault(FlyCommand.PDC_IN_FLY_MODE, PersistentDataType.BOOLEAN, false)) {
            player.setAllowFlight(true);
            player.setFlying(true);
        }
    }
}
