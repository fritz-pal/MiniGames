package me.fritzpal.miniGames;

import me.fritzpal.miniGames.games.RollOut;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Events implements Listener {
    private final Main plugin;

    public Events(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (plugin.getRunningGame() != null) {
            event.getPlayer().setGameMode(GameMode.SPECTATOR);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (plugin.getRunningGame() != null) {
            plugin.getRunningGame().removePlayer(event.getPlayer());
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player p) {
            if (plugin.getRunningGame() != null) {
                if (event.getCause() == EntityDamageEvent.DamageCause.LAVA) {
                    plugin.getRunningGame().eliminate(p);
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void test(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if (event.getAction() != Action.LEFT_CLICK_AIR) return;
        if (plugin.getRunningGame() != null) {
            if (plugin.getRunningGame() instanceof RollOut game) {
                game.newWall(p.getLocation());
            }
        }
    }
}
