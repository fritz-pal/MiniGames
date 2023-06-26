package me.fritzpal.miniGames.games;

import me.fritzpal.miniGames.Main;
import me.fritzpal.miniGames.utils.Game;
import me.fritzpal.miniGames.utils.Wall;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class RollOut implements Game {
    private final Main plugin;
    private final Location center;
    private final List<Player> alive = new ArrayList<>();
    private boolean cancel = false;

    public RollOut(Main plugin) {
        this.plugin = plugin;
        center = plugin.getConfig().getLocation("locations.roll_out");
        if (center == null) throw new NullPointerException("RollOut: Location is null!");
    }

    @Override
    public void removePlayer(Player p) {
        if (alive.contains(p)) {
            alive.remove(p);
            Bukkit.broadcastMessage("§a" + p.getName() + " §7left!");
            if (alive.size() == 1) endGame(alive.get(0));
        }
    }

    private void endGame(Player winner) {
        alive.clear();
        cancel = true;
        Bukkit.broadcastMessage("§a" + winner.getName() + " §7won the game!");
        for (Player all : plugin.getServer().getOnlinePlayers()) {
            all.playSound(all.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            all.sendTitle("§a§l" + winner.getName(), "§7won the game!", 10, 50, 20);
        }
        Main.spawnFireworks(center);
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player all : plugin.getServer().getOnlinePlayers()) {
                    all.setGameMode(GameMode.SURVIVAL);
                    all.teleport(center.getWorld().getSpawnLocation());
                }
                plugin.setRunningGame(null);
            }
        }.runTaskLater(plugin, 100);
    }

    @Override
    public void teleportPlayers() {
        for (Player all : plugin.getServer().getOnlinePlayers()) {
            Location loc = center.clone().add(Math.random() * 14 - 7, 0, Math.random() * 14 - 7);
            all.teleport(loc);
            all.playSound(all.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        }
    }

    @Override
    public void setup() {
        for (Player all : plugin.getServer().getOnlinePlayers()) {
            all.sendTitle("§a§lRoll Out", "§eDon't get pushed of the platform by the moving §cblocks§e!", 10, 150, 10);
            all.getInventory().clear();
            all.setHealth(20);
            all.setFoodLevel(20);
            all.setExp(0);
            all.setGameMode(GameMode.ADVENTURE);
        }
    }

    @Override
    public void start() {
        for (Player all : plugin.getServer().getOnlinePlayers()) {
            all.playSound(all.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
            alive.add(all);
        }
        new BukkitRunnable() {
            int speed = 12;
            int t = 0;
            boolean skip = false;

            @Override
            public void run() {
                if (t % speed == 0) {
                    if (!skip) newWall(speed);
                    skip = false;
                }
                if (t % 60 == 0 && speed > 5) {
                    skip = true;
                    speed--;
                }
                t++;
                if (cancel) cancel();
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    @Override
    public void eliminate(Player p) {
        if (!alive.contains(p)) return;
        p.setGameMode(GameMode.SPECTATOR);
        alive.remove(p);
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 1);
        p.sendTitle("§4§lELIMINATED", "§cYou have been eliminated!", 10, 50, 20);
        Bukkit.broadcastMessage("§a" + p.getName() + " §7has been eliminated!");
        if (alive.size() == 1) endGame(alive.get(0));
    }

    public void newWall(int speed) {
        Vector direction = null;
        Location location = null;
        switch ((int) (Math.random() * 4)) {
            case 0 -> {
                direction = new Vector(0, 0, 1);
                location = center.clone().add(-8, 0, -10);
            }
            case 1 -> {
                direction = new Vector(1, 0, 0);
                location = center.clone().add(-10, 0, -8);
            }
            case 2 -> {
                direction = new Vector(0, 0, -1);
                location = center.clone().add(-8, 0, 10);
            }
            case 3 -> {
                direction = new Vector(-1, 0, 0);
                location = center.clone().add(10, 0, -8);
            }
        }
        new Wall(plugin, location, direction, speed, 20);
    }
}
