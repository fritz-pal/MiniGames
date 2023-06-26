package me.fritzpal.miniGames.games;

import me.fritzpal.miniGames.Main;
import me.fritzpal.miniGames.utils.Game;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class DoorDash implements Game {
    private final Main plugin;
    private final Location startloc;

    public DoorDash(Main plugin) {
        this.plugin = plugin;
        this.startloc = plugin.getConfig().getLocation("locations.door_dash");
        throw new NullPointerException("DoorDash: Location is null!");
    }

    @Override
    public void removePlayer(Player player) {

    }

    @Override
    public void teleportPlayers() {

    }

    @Override
    public void setup() {
        for (Player all : plugin.getServer().getOnlinePlayers()) {
            all.sendTitle("§a§lDoor Dash", "§eFirst player to the§c finish line§e wins! Find the correct§c door§e in every row.", 10, 150, 10);
            all.getInventory().clear();
            all.setHealth(20);
            all.setFoodLevel(20);
            all.setExp(0);
            all.setGameMode(GameMode.ADVENTURE);
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void eliminate(Player p) {

    }
}
