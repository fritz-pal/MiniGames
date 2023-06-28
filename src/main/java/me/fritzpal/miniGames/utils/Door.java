package me.fritzpal.miniGames.utils;

import me.fritzpal.miniGames.Main;
import me.fritzpal.miniGames.games.DoorDash;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Door {
    private final Location location;
    private final Main plugin;
    private final DoorDash game;
    private final int height;
    private final int width;

    public Door(Main plugin, DoorDash game, Location location, boolean openable, int width, int height) {
        this.plugin = plugin;
        this.game = game;
        this.location = location;
        this.height = height;
        this.width = width;

        build();
        if (openable) checkPlayers();
    }

    private void checkPlayers() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!game.hasStarted()) return;
                for (Player all : location.getWorld().getPlayers()) {
                    if (all.getGameMode() != org.bukkit.GameMode.ADVENTURE) continue;
                    Block block = all.getTargetBlock(null, 1);
                    if (!block.getType().equals(Material.PINK_CONCRETE)) continue;

                    for (int i = 0; i < width; i++) {
                        for (int j = 0; j < height; j++) {
                            if (location.clone().add(0, j, i).getBlock().getLocation().equals(block.getLocation())) {
                                Bukkit.broadcastMessage("§a§lA§c§l door§a§l was opened!");
                                Main.broadcastSound(Sound.BLOCK_IRON_DOOR_OPEN);
                                Main.broadcastSound(Sound.BLOCK_NOTE_BLOCK_CHIME);
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
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                location.getWorld().spawnParticle(Particle.FLAME, location.clone().add(0.5, j + 0.5, i + 0.5), 10, 0.5f, 0.5f, 0.5f, 0.1f);
            }
        }
    }

    private void build() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                location.clone().add(0, j, i).getBlock().setType(Material.PINK_CONCRETE);
            }
        }
    }

    public void remove() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                location.clone().add(0, j, i).getBlock().setType(Material.AIR);
            }
        }
    }
}
