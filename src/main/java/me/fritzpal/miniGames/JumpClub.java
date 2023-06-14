package me.fritzpal.miniGames;

import me.fritzpal.miniGames.utils.Game;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class JumpClub implements Game {
    Main plugin;
    Location middle;

    public JumpClub(Main plugin) {
        this.plugin = plugin;
        middle = new Location(plugin.getServer().getWorld("world"), -208, 84, 15);
    }

    @Override
    public void teleportPlayers() {

    }

    @Override
    public void setup() {
        new BukkitRunnable() {
            double t = 0;

            public void run() {
                t += Math.PI / 64;

                for (double r = 0; r < 12; r += 0.2) {
                    double x = r * Math.sin(t);
                    double z = r * Math.cos(t);
                    middle.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, middle.clone().add(x, 0, z), 5, 0.2, 0.2, 0.2, 0);
                    for (Player all : plugin.getServer().getOnlinePlayers()) {
                        if (all.getLocation().distance(middle.clone().add(x, 0, z)) < 1) {
                            all.sendMessage("§aTouching laser! §c" + t);
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0, 1);
    }

    @Override
    public void start() {

    }
}
