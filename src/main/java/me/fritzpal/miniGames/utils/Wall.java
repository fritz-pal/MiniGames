package me.fritzpal.miniGames.utils;

import me.fritzpal.miniGames.Main;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;


public class Wall {
    private final byte[][] structure;
    private final Location startLocation;
    private final Vector direction;
    private int distance = 1;

    public Wall(Main plugin, Location location, Vector direction, int speed, int maxDistance) {
        startLocation = location.getBlock().getLocation();
        this.direction = direction;
        structure = Main.STRUCTURES.get((int) (Math.random() * Main.STRUCTURES.size()));
        build();
        new BukkitRunnable() {
            @Override
            public void run() {
                move();
                if (distance >= maxDistance) {
                    cancel();
                    remove();
                }
            }
        }.runTaskTimer(plugin, speed, speed);
    }

    private void build() {
        for (int i = structure.length - 1; i >= 0; i--) {
            for (int j = 0; j < structure[i].length; j++) {
                startLocation.clone()
                        .add(direction.clone().multiply(distance))
                        .add(direction.getX() == 0 ? j : 0, structure.length - i - 1, direction.getZ() == 0 ? j : 0)
                        .getBlock().setType(structure[i][j] == 1 ? Material.LIGHT_BLUE_CONCRETE : Material.AIR);
            }
        }
    }

    public void move() {
        remove();
        distance++;
        build();
        // push players in the direction of the wall
        List<Player> alreadyTeleported = new ArrayList<>();
        for (int i = structure.length - 1; i >= 0; i--) {
            for (int j = 0; j < structure[i].length; j++) {
                if (structure[i][j] == 0) continue;
                for (Player all : startLocation.getWorld().getPlayers()) {
                    if(all.getGameMode() != GameMode.ADVENTURE) continue;
                    if (alreadyTeleported.contains(all)) continue;
                    Location blockLoc = startLocation.clone()
                            .add(direction.clone().multiply(distance))
                            .add(direction.getX() == 0 ? j : 0, structure.length - i - 1, direction.getZ() == 0 ? j : 0)
                            .getBlock().getLocation();
                    if (all.getLocation().getBlock().getLocation().equals(blockLoc) || all.getLocation().getBlock().getLocation().clone().add(0, 1, 0).equals(blockLoc)) {
                        alreadyTeleported.add(all);
                        Vector velocity = all.getVelocity();
                        Location newLoc = all.getLocation().clone().add(direction);
                        if (direction.equals(new Vector(1, 0, 0))) {
                            newLoc.setX(((int) newLoc.getX()) + (newLoc.getX() < 0 ? -0.7 : 0.3));
                        } else if (direction.equals(new Vector(-1, 0, 0))) {
                            newLoc.setX(((int) newLoc.getX()) + (newLoc.getX() < 0 ? -0.3 : 0.7));
                        } else if (direction.equals(new Vector(0, 0, 1))) {
                            newLoc.setZ(((int) newLoc.getZ()) + (newLoc.getZ() < 0 ? -0.7 : 0.3));
                        } else if (direction.equals(new Vector(0, 0, -1))) {
                            newLoc.setZ(((int) newLoc.getZ()) + (newLoc.getZ() < 0 ? -0.3 : 0.7));
                        }
                        all.teleport(newLoc);
                        all.setVelocity(velocity);
                    }
                }
            }
        }
    }

    public void remove() {
        for (int i = structure.length - 1; i >= 0; i--) {
            for (int j = 0; j < structure[i].length; j++) {
                startLocation.clone()
                        .add(direction.clone().multiply(distance))
                        .add(direction.getX() == 0 ? j : 0, structure.length - i - 1, direction.getZ() == 0 ? j : 0)
                        .getBlock().setType(Material.AIR);
            }
        }
    }
}
