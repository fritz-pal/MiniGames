package me.fritzpal.miniGames.utils;

import org.bukkit.entity.Player;

public interface Game {
    void removePlayer(Player player);

    void teleportPlayers();

    void setup();

    void start();

    void eliminate(Player p);
}
