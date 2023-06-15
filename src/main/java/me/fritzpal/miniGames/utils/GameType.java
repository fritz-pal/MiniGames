package me.fritzpal.miniGames.utils;

import me.fritzpal.miniGames.DoorDash;
import me.fritzpal.miniGames.JumpClub;
import me.fritzpal.miniGames.Main;
import me.fritzpal.miniGames.RollOut;

public enum GameType {
    ROLL_OUT,
    DOOR_DASH,
    JUMP_CLUB;

    public static GameType fromString(String name) {
        for (GameType gameType : GameType.values()) {
            if (gameType.name().equalsIgnoreCase(name)) {
                return gameType;
            }
        }
        return null;
    }

    public Game getGame(Main plugin) {
        return switch (this) {
            case ROLL_OUT -> new RollOut();
            case DOOR_DASH -> new DoorDash();
            case JUMP_CLUB -> new JumpClub(plugin);
        };
    }
}
