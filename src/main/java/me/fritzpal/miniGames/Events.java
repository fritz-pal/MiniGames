package me.fritzpal.miniGames;

import me.fritzpal.miniGames.games.RollOut;
import me.fritzpal.miniGames.utils.Door;
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
    public void test(PlayerInteractEvent e){
        if (e.getAction() != Action.LEFT_CLICK_AIR) return;
        new Door(plugin, e.getPlayer().getLocation().clone().add(10, 0, 0), true);
    }
}
