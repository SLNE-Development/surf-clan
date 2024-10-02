package dev.slne.surf.essentials.utils.abtract;

import dev.slne.surf.essentials.SurfEssentials;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Nullable;

public abstract class FoliaUtil extends PropertiesUtil {
  public static void damage(Damageable target, double amount, @Nullable Entity source) {
    runOnEntity(target, () -> target.damage(amount, source));
  }

  public static void addPotionEffect(LivingEntity entity, PotionEffect potionEffect) {
    runOnEntity(entity, () -> entity.addPotionEffect(potionEffect));
  }

  public static void removePotionEffect(LivingEntity entity, PotionEffectType potionEffect) {
    runOnEntity(entity, () -> entity.removePotionEffect(potionEffect));
  }

  public static void clearActivePotionEffects(LivingEntity entity) {
    runOnEntity(entity, entity::clearActivePotionEffects);
  }

  public static void doWithBlock(Location location, Consumer<Block> blockConsumer) {
    runAtLocation(location, () -> blockConsumer.accept(location.getBlock()));
  }

  public static void addEnchantment(LivingEntity source, ItemStack stack, Enchantment enchantment, int level) {
    runOnEntity(source, () -> stack.addEnchantment(enchantment, level));
  }

  public static void doWithIsChunkForceLoaded(World world, int chunkX, int chunkZ, BooleanConsumer consumer) {
    runAtLocation(toBlockLocation(world, chunkX, chunkZ), () -> consumer.accept(world.isChunkForceLoaded(chunkX, chunkZ)));
  }

  public static void setForceLoaded(Chunk chunk, boolean forceLoaded) {
    runAtLocation(toBlockLocation(chunk.getWorld(), chunk.getX(), chunk.getZ()), () -> chunk.setForceLoaded(forceLoaded));
  }

  public static void setForceLoaded(World world, int chunkX, int chunkZ, boolean forceLoaded) {
    runAtLocation(toBlockLocation(world, chunkX, chunkZ), () -> world.setChunkForceLoaded(chunkX, chunkZ, forceLoaded));
  }

  public static void runOnEntity(Entity entity, Runnable runnable) {
    entity.getScheduler().run(SurfEssentials.getInstance(), __ -> runnable.run(), null);
  }

  public static void runOnEntityDelayed(Entity entity, long delay, Runnable runnable) {
    entity.getScheduler().runDelayed(SurfEssentials.getInstance(), __ -> runnable.run(), null, delay);
  }

  public static void runAtLocation(Location location, Runnable runnable) {
    Bukkit.getRegionScheduler().run(SurfEssentials.getInstance(), location, __ -> runnable.run());
  }

  public static void runGlobal(Runnable runnable) {
    Bukkit.getGlobalRegionScheduler().run(SurfEssentials.getInstance(), __ -> runnable.run());
  }

  private static Location toBlockLocation(World world, int chunkX, int chunkZ) {
    return new Location(world, chunkX << 4, 0, chunkZ << 4);
  }
}
