package dev.slne.surf.essentials.main.listeners;

import dev.slne.surf.essentials.main.commands.cheat.InfinityCommand;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class InfinityListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (InfinityCommand.getPlayersInInfinity().contains(((CraftPlayer) event.getPlayer()).getHandle())) {
            Player player = event.getPlayer();
            PlayerInventory inventory = player.getInventory();
            ItemStack itemStack = inventory.getItemInMainHand();
            itemStack.setAmount(1);
            regainItems(player, itemStack);
        }
    }

    @EventHandler
    public void onSpawnEggPlace(PlayerInteractEvent event) {
        if (InfinityCommand.getPlayersInInfinity().contains(((CraftPlayer) event.getPlayer()).getHandle())) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getPlayer().getInventory().getItemInMainHand().getItemMeta() instanceof SpawnEggMeta) {
                Player player = event.getPlayer();
                PlayerInventory inventory = player.getInventory();
                inventory.getItemInMainHand().setAmount(1);
                regainItems(player, inventory.getItemInMainHand());
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        if (InfinityCommand.getPlayersInInfinity().contains(((CraftPlayer) event.getPlayer()).getHandle())) {
            Player player = event.getPlayer();
            PlayerInventory inventory = player.getInventory();
            inventory.getItemInMainHand().setAmount(1);
            regainItems(player, inventory.getItemInMainHand());
        }
    }

    public static void regainItems(final Player player, final ItemStack item) {
        (new BukkitRunnable() {
            public void run() {
                player.getInventory().addItem(item);
                player.getInventory().getItemInMainHand().setAmount(2);
            }
        }).run();
    }
}
