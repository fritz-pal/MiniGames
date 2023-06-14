package me.fritzpal.miniGames;

import me.fritzpal.miniGames.commands.MiniGameCommand;
import me.fritzpal.miniGames.utils.Game;
import me.fritzpal.miniGames.utils.GameType;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private Game runningGame = null;

    @Override
    public void onEnable() {
        getCommand("minigame").setExecutor(new MiniGameCommand(this));
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
        runningGame.start();

    }
}
