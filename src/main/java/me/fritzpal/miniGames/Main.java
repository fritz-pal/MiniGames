package me.fritzpal.miniGames;

import me.fritzpal.miniGames.commands.CommandCompleter;
import me.fritzpal.miniGames.commands.MiniGameCommand;
import me.fritzpal.miniGames.utils.Game;
import me.fritzpal.miniGames.utils.GameType;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {
    public static final List<byte[][]> STRUCTURES = new ArrayList<>();
    private Game runningGame = null;

    public static void broadcastSound(Sound sound) {
        for (Player all : Bukkit.getOnlinePlayers()) {
            all.playSound(all.getLocation(), sound, 1, 1);
        }
    }

    public static void spawnFireworks(Location loc) {
        for (int i = 0; i < 10; i++) {
            Firework fw = (Firework) loc.getWorld().spawnEntity(loc.clone().add(Math.random() * 8 - 4, 1, Math.random() * 8 - 4), EntityType.FIREWORK);
            FireworkMeta meta = fw.getFireworkMeta();
            meta.addEffect(FireworkEffect.builder()
                    .withColor(Color.fromRGB(Math.random() < 0.5 ? 0 : 255, Math.random() < 0.5 ? 0 : 255, Math.random() < 0.5 ? 0 : 255))
                    .with(FireworkEffect.Type.BALL_LARGE)
                    .withFlicker()
                    .withFade(Color.LIME)
                    .build()
            );
            meta.setPower(1);
            fw.setFireworkMeta(meta);
        }
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        initStructures();
        getCommand("minigame").setExecutor(new MiniGameCommand(this));
        getCommand("minigame").setTabCompleter(new CommandCompleter());
        getServer().getPluginManager().registerEvents(new Events(this), this);
        getLogger().info("plugin enabled!");
    }

    private void initStructures() {
        STRUCTURES.add(new byte[][]{
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1},
                {1, 0, 1, 0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1},
                {1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        });
        STRUCTURES.add(new byte[][]{
                {1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1},
                {0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1},
                {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1}
        });
        STRUCTURES.add(new byte[][]{
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1},
                {1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 1}
        });
        STRUCTURES.add(new byte[][]{
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1}
        });
        STRUCTURES.add(new byte[][]{
                {0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 0, 1, 0},
                {0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 0, 1, 0},
                {0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 0, 1, 0},
                {0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 0, 1, 0}
        });
        STRUCTURES.add(new byte[][]{
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 0, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 1},
                {1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1}
        });
        STRUCTURES.add(new byte[][]{
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 1},
                {1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 1}
        });
        STRUCTURES.add(new byte[][]{
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1},
                {1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1}
        });
        STRUCTURES.add(new byte[][]{
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1}
        });
        STRUCTURES.add(new byte[][]{
                {1, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        });
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
        new BukkitRunnable() {
            int time = 10;

            @Override
            public void run() {
                if (time == 10 || time == 5 || time == 4 || time == 3 || time == 2 || time == 1) {
                    Bukkit.broadcastMessage("§eThe game will start in §c" + time + (time == 1 ? "§e second!" : "§e seconds!"));
                    broadcastSound(Sound.BLOCK_NOTE_BLOCK_PLING);
                }
                for (Player all : Bukkit.getOnlinePlayers()) {
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
}
