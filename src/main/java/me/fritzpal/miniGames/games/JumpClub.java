package me.fritzpal.miniGames.games;

import me.fritzpal.miniGames.Main;
import me.fritzpal.miniGames.utils.Game;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class JumpClub implements Game {
    private final Main plugin;
    private final Location center;
    private final List<Player> alive = new ArrayList<>();
    private boolean topBarEnabled = false;
    private boolean topBarDamage = false;
    private double speedBottom = 90;
    private double speedTop = 100;
    private boolean cancel = false;

    public JumpClub(Main plugin) {
        this.plugin = plugin;
        center = plugin.getConfig().getLocation("jump_club.centerLocation");
        if (center == null) throw new NullPointerException("JumpClub: Center location is null!");
    }

    @Override
    public void removePlayer(Player p) {
        if (alive.contains(p)) {
            alive.remove(p);
            Bukkit.broadcastMessage("§a" + p.getName() + " §7left!");
            if (alive.size() == 1) endGame(alive.get(0));
        }
    }

    @Override
    public void teleportPlayers() {
        for (Player all : plugin.getServer().getOnlinePlayers()) {
            Location loc = center.clone().add(Math.random() * 16 - 8, 0, Math.random() * 16 - 8);
            Vector dir = center.toVector().subtract(loc.toVector()).normalize();
            loc.setDirection(dir);
            all.teleport(loc);
            all.playSound(all.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        }
    }

    @Override
    public void setup() {
        for (Player all : plugin.getServer().getOnlinePlayers()) {
            all.sendTitle("§a§lJump Club", "§cSneak§e and §cjump §eto avoid the spinning §4§llasers§e!", 10, 150, 10);
            all.getInventory().clear();
            all.setHealth(20);
            all.setFoodLevel(20);
            all.setExp(0);
            all.setGameMode(GameMode.ADVENTURE);
        }

        new BukkitRunnable() {
            double t0 = 0, t1 = 0;

            public void run() {
                if (cancel) {
                    cancel();
                    return;
                }

                //bottom bar
                t0 += Math.PI / speedBottom;
                for (double r = 0; r < 12; r += 0.3) {
                    double x = r * Math.sin(t0);
                    double z = r * Math.cos(t0);
                    center.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, center.clone().add(x, 0.3, z), 3, 0.2, 0.2, 0.2, 0);
                    for (Player all : plugin.getServer().getOnlinePlayers()) {
                        if (all.getLocation().distance(center.clone().add(x, 0, z)) < 1) {
                            eliminate(all);
                        }
                    }
                }
                if (!topBarEnabled) return;
                //top bar
                t1 += Math.PI / speedTop;
                for (double r = 0; r < 12; r += 0.3) {
                    double x = r * Math.sin(t1);
                    double z = r * Math.cos(t1);
                    center.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, center.clone().add(x, 2, z), 3, 0.2, 0.2, 0.2, 0);
                    if (!topBarDamage) continue;
                    for (Player all : plugin.getServer().getOnlinePlayers()) {
                        if (all.isSneaking() && all.isOnGround()) continue;
                        if (all.getLocation().distance(center.clone().add(x, 1, z)) < 1.5) {
                            eliminate(all);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 1);
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

    private void endGame(Player winner) {
        alive.clear();
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
                    all.removePotionEffect(PotionEffectType.JUMP);
                    all.setGameMode(GameMode.SURVIVAL);
                    all.teleport(center.getWorld().getSpawnLocation());
                }
                cancel = true;
                plugin.setRunningGame(null);
            }
        }.runTaskLater(plugin, 100);
    }

    @Override
    public void start() {
        for (Player all : plugin.getServer().getOnlinePlayers()) {
            all.playSound(all.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
            all.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 1000000, 0, false, false, false));
            alive.add(all);
        }
        new BukkitRunnable() {
            int t = 0;

            @Override
            public void run() {
                if (cancel) {
                    cancel();
                    return;
                }

                t++;
                switch (t) {
                    case 15, 43 -> speedBottom = -speedBottom;
                    case 30 -> {
                        Main.broadcastSound(Sound.BLOCK_NOTE_BLOCK_PLING);
                        Bukkit.broadcastMessage("§aBeware of the second §4§llaser§a!");
                        topBarEnabled = true;
                    }
                    case 32 -> topBarDamage = true;
                    case 45 -> speedTop = -speedTop;
                    case 60 -> speedBottom -= 10;
                }

                //every 8s
                if (t % 5 == 0) {
                    if (speedBottom > 20 || speedBottom < -20) {
                        if (speedBottom > 0) speedBottom--;
                        if (speedBottom < 0) speedBottom++;
                    }
                }
                //after 20s
                if (t >= 20) {
                    //every 5s
                    if (t % 5 == 0) {
                        if (speedTop > 20 || speedTop < -20) {
                            if (speedTop > 0) speedTop--;
                            if (speedTop < 0) speedTop++;
                        }
                    }
                }
                //after 60s
                if (t >= 55) {
                    //every 10s
                    if (t % 30 == 0) {
                        speedTop = -speedTop;
                    }
                    if ((t + 2) % 30 == 0) {
                        speedBottom = -speedBottom;
                    }
                }
            }
        }.runTaskTimer(plugin, 20, 20);
    }
}
