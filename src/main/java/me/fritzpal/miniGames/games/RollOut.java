package me.fritzpal.miniGames.games;

import me.fritzpal.miniGames.Main;
import me.fritzpal.miniGames.utils.Game;
import me.fritzpal.miniGames.utils.Wall;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class RollOut implements Game {
    private final Main plugin;

    public RollOut(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void removePlayer(Player player) {

    }

    @Override
    public void teleportPlayers() {

    }

    @Override
    public void setup() {

    }

    @Override
    public void start() {

    }

    @Override
    public void eliminate(Player p) {

    }

    public void newWall(Location location) {
        Wall wall = new Wall(location, new Vector(0, 0, 1));

        new BukkitRunnable() {
            @Override
            public void run() {
                wall.move();
            }
        }.runTaskTimer(plugin, 40, 40);
    }
}
