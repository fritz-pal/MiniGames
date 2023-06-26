package me.fritzpal.miniGames.utils;

import me.fritzpal.miniGames.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Door {
    private final Location location;
    private final Main plugin;

    public Door(Main plugin, Location location, boolean openable) {
        this.plugin = plugin;
        this.location = location;

        build();
        if (openable) checkPlayers();
    }

    private void checkPlayers() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player all : location.getWorld().getPlayers()) {
                    if (all.getGameMode() != org.bukkit.GameMode.ADVENTURE) continue;
                    Block block = all.getTargetBlock(null, 1);
                    if (!block.getType().equals(Material.PINK_CONCRETE)) continue;

                    for (int i = 0; i < 2; i++) {
                        for (int j = 0; j < 3; j++) {
                            if (location.clone().add(i, j, 0).getBlock().getLocation().equals(block.getLocation())) {
                                Bukkit.broadcastMessage("§e1st §c§ldoor opened!");
                                Main.broadcastSound(Sound.BLOCK_IRON_DOOR_OPEN);
                                Main.broadcastSound(Sound.BLOCK_NOTE_BLOCK_PLING);
                                spawnParticles();
                                remove();
                                cancel();
                                return;
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    private void spawnParticles() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                location.getWorld().spawnParticle(org.bukkit.Particle.FLAME, location.clone().add(i + 0.5, j + 0.5, 0.5), 10, 0.5f, 0.5f, 0.5f, 0.1f);
            }
        }
    }

    private void build() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                location.clone().add(i, j, 0).getBlock().setType(Material.PINK_CONCRETE);
            }
        }
    }

    public void remove() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                location.clone().add(i, j, 0).getBlock().setType(Material.AIR);
            }
        }
    }
}
