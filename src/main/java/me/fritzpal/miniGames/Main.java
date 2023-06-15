package me.fritzpal.miniGames;

import me.fritzpal.miniGames.commands.CommandCompleter;
import me.fritzpal.miniGames.commands.MiniGameCommand;
import me.fritzpal.miniGames.utils.Game;
import me.fritzpal.miniGames.utils.GameType;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin {
    private Game runningGame = null;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getCommand("minigame").setExecutor(new MiniGameCommand(this));
        getCommand("minigame").setTabCompleter(new CommandCompleter());
        getLogger().info("plugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("plugin disabled!");
    }

    public Game getRunningGame() {
        return runningGame;
    }

    public void setRunningGame(Game game) {
        runningGame = game;
    }

    public void startGame(GameType type) {
        runningGame = type.getGame(this);
        runningGame.setup();
        runningGame.teleportPlayers();
        runningGame.sendTitle();
        new BukkitRunnable() {
            int time = 10;

            @Override
            public void run() {
                if (time == 10 || time == 5 || time == 4 || time == 3 || time == 2 || time == 1) {
                    Bukkit.broadcastMessage("§eThe game will start in §c" + time + (time == 1 ? "§e second!" : "§e seconds!"));
                    broadcastSound(Sound.BLOCK_NOTE_BLOCK_PLING);
                }
                for(Player all : Bukkit.getOnlinePlayers()) {
                    all.setLevel(time);
                }
                time--;
                if (time < 0) {
                    runningGame.start();
                    cancel();
                }
            }
        }.runTaskTimer(this, 20, 20);
    }

    public static void broadcastSound(Sound sound) {
        for (Player all : Bukkit.getOnlinePlayers()) {
            all.playSound(all.getLocation(), sound, 1, 1);
        }
    }
}
