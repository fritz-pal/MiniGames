package me.fritzpal.miniGames.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;


public class Wall {
    private final byte[][] structure;
    private final Location startLocation;
    private final Vector direction;
    private int distance = 1;

    public Wall(Location location, Vector direction) {
        startLocation = location.getBlock().getLocation();
        this.direction = direction;
        structure = new byte[][]{
                {1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 0, 0, 1, 1, 1},
                {1, 0, 1, 0, 0, 1, 1, 1},
                {1, 0, 1, 1, 1, 1, 0, 1}
        };
        build();
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
        for (int i = structure.length - 1; i >= 0; i--) {
            for (int j = 0; j < structure[i].length; j++) {
                for (Player all : startLocation.getWorld().getPlayers()) {
                    Location blockLoc = startLocation.clone()
                            .add(direction.clone().multiply(distance))
                            .add(direction.getX() == 0 ? j : 0, structure.length - i - 1, direction.getZ() == 0 ? j : 0)
                            .getBlock().getLocation();
                    if (all.getLocation().getBlock().getLocation().equals(blockLoc) || all.getLocation().getBlock().getLocation().clone().add(0, 1, 0).equals(blockLoc))
                        all.setVelocity(direction.clone().multiply(2));
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
