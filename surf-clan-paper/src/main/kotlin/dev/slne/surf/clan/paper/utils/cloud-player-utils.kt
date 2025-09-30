package dev.slne.surf.clan.paper.utils

import dev.slne.surf.cloud.api.common.player.OfflineCloudPlayer
import org.bukkit.Bukkit

fun OfflineCloudPlayer.toBukkitOfflinePlayer() = Bukkit.getOfflinePlayer(this.uuid)