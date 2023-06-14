package me.fritzpal.miniGames.commands;

import me.fritzpal.miniGames.Main;
import me.fritzpal.miniGames.utils.GameType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MiniGameCommand implements CommandExecutor {
    Main plugin;

    public MiniGameCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(!cmd.getName().equalsIgnoreCase("minigame")) return false;
        if(args.length < 1) {
            sender.sendMessage("§cUsage: " + cmd.getUsage());
            return true;
        }
        if(plugin.getRunningGame() != null) {
            sender.sendMessage("§cA game is already running!");
            return true;
        }
        GameType type = GameType.fromString(args[0].toUpperCase());
        if(type == null) {
            sender.sendMessage("§cInvalid game!");
            return true;
        }
        plugin.startGame(type);
        return true;
    }
}
