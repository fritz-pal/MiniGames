package me.fritzpal.miniGames.commands;

import me.fritzpal.miniGames.utils.GameType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class CommandCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        List<String> list = new ArrayList<>();
        if (command.getName().equalsIgnoreCase("minigame")) {
            for (GameType type : GameType.values()){
                list.add(type.name());
            }
        }
        List<String> result = new ArrayList<>();
        if (args.length == 1) {
            for (String s : list) {
                if (s.startsWith(args[0].toUpperCase())) {
                    result.add(s);
                }
            }
            return result;
        }
        return null;
    }
}
