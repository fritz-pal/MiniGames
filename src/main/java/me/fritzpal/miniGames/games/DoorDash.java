package me.fritzpal.miniGames.games;

import me.fritzpal.miniGames.Main;
import me.fritzpal.miniGames.utils.Door;
import me.fritzpal.miniGames.utils.Game;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class DoorDash implements Game {
    private final Main plugin;
    private final Location startloc;
    private final List<Door[]> doors = new ArrayList<>();
    private final int numberDoors;
    private final int distance;
    private final int width;
    private final int height;
    private boolean started = false;

    public DoorDash(Main plugin) {
        this.plugin = plugin;
        this.startloc = plugin.getConfig().getLocation("locations.door_dash");
        if (startloc == null) throw new NullPointerException("DoorDash: Location is null!");
        numberDoors = plugin.getConfig().getInt("door_dash.number");
        if (numberDoors < 2) throw new IllegalArgumentException("DoorDash: Number of doors must be greater than 2!");
        distance = plugin.getConfig().getInt("door_dash.distance_between_lines");
        if (distance < 1) throw new IllegalArgumentException("DoorDash: Distance must be greater than 1!");
        width = plugin.getConfig().getInt("door_dash.door_width");
        height = plugin.getConfig().getInt("door_dash.door_height");
        if (width <= 0 || height <= 0) throw new IllegalArgumentException("Door: Width or height is less than 1!");
    }

    @Override
    public void removePlayer(Player player) {

    }

    @Override
    public void teleportPlayers() {
        for (Player all : Bukkit.getOnlinePlayers()) {
            all.teleport(startloc.clone().add(-4, 0, Math.random() * ((width + 1) * numberDoors - 1)).setDirection(new Vector(1, 0, 0)));
        }
    }

    @Override
    public void setup() {
        for (Player all : plugin.getServer().getOnlinePlayers()) {
            all.sendTitle("§a§lDoor Dash", "§eFirst player to the§c finish line§e wins!\n§eEvery line only contains §cone§e correct door.", 10, 150, 10);
            all.getInventory().clear();
            all.setHealth(20);
            all.setFoodLevel(20);
            all.setExp(0);
            all.setGameMode(GameMode.ADVENTURE);
        }

        for (int i = numberDoors; i > 1; i--) {
            Door[] line = new Door[i];
            int randomDoor = (int) (Math.random() * i);
            System.out.println(randomDoor);
            for (int j = 0; j < i; j++) {
                line[j] = new Door(plugin, this, startloc.clone().add((8 - i) * (distance + 1), 0, j * (width + 1) + (double) ((8 - i) * (width + 1)) / 2), j == randomDoor, width, height);
            }
            doors.add(line);
        }
    }

    @Override
    public void start() {
        started = true;
        Main.broadcastSound(Sound.ENTITY_ENDER_DRAGON_GROWL);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player all : Bukkit.getOnlinePlayers()) {
                    if(all.getGameMode() != GameMode.ADVENTURE) continue;
                    if (all.getLocation().getX() >= startloc.getX() + (numberDoors - 1) * (distance + 1)) {
                        endGame(all);
                        cancel();
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    private void endGame(Player winner) {
        Bukkit.broadcastMessage("§a" + winner.getName() + " §7won the game!");
        for (Player all : plugin.getServer().getOnlinePlayers()) {
            all.playSound(all.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            all.sendTitle("§a§l" + winner.getName(), "§7won the game!", 10, 50, 20);
        }

        Main.spawnFireworks(winner.getLocation());

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player all : plugin.getServer().getOnlinePlayers()) {
                    all.setGameMode(GameMode.SURVIVAL);
                    all.teleport(startloc.getWorld().getSpawnLocation());
                }
                removeDoors();
                plugin.setRunningGame(null);
            }
        }.runTaskLater(plugin, 100);
    }

    private void removeDoors() {
        for (Door[] line : doors) {
            for (Door door : line) {
                door.remove();
            }
        }
    }

    public boolean hasStarted() {
        return started;
    }

    @Override
    public void eliminate(Player p) {

    }
}
