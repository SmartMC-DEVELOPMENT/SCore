package us.smartmc.lobbymodule.listener;

import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;
import us.smartmc.core.SmartCore;
import us.smartmc.core.handler.SpawnHandler;
import us.smartmc.lobbymodule.instance.parkour.ParkourTop;
import us.smartmc.lobbymodule.instance.parkour.PlayerParkourEndedEvent;
import us.smartmc.lobbymodule.instance.parkour.PlayerParkourNewRecordEvent;
import us.smartmc.lobbymodule.instance.parkour.PlayerParkourSession;

public class ParkourListeners implements Listener {

    @EventHandler
    public void setupTop(PlayerJoinEvent event) {
        ParkourTop.setup();
    }

    @EventHandler
    public void rewardPlayerTest(PlayerParkourEndedEvent event) {
        Player player = event.getPlayer();
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1, 1);

        if (event.getSession().hasReachedNewRecord()) {
            Bukkit.getPluginManager().callEvent(new PlayerParkourNewRecordEvent(event.getSession()));
            // Guardar el player data para actualizar top en caso de que siga conectado
            CorePlayer.get(player).getPlayerData().save();
        }

        Bukkit.getScheduler().runTaskLater(SmartCore.getPlugin(), () -> {
            player.setVelocity(player.getVelocity().add(new Vector(0, 2.5, 0)));
        }, 2);
    }

    @EventHandler
    public void exitParkourMode(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!(event.getAction().name().contains("LEFT") || event.getAction().name().contains("RIGHT"))) return;
        if (PlayerParkourSession.isActive(player) &&
                player.getItemInHand().getType().equals(Material.BARRIER)) {
            PlayerParkourSession.remove(event.getPlayer(), true);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        PlayerParkourSession.remove(event.getPlayer());
    }

    @EventHandler
    public void startParkour(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.PHYSICAL)) return;
        if (!event.getClickedBlock().getType().equals(Material.LIGHT_WEIGHTED_PRESSURE_PLATE)) return;
        Player player = event.getPlayer();
        if (player.getLocation().getY() >= 75) return;
        if (PlayerParkourSession.isActive(player)) return;
        PlayerParkourSession.getSession(player, true).registerStart();
    }

    @EventHandler
    public void endParkour(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.PHYSICAL)) return;
        if (!event.getClickedBlock().getType().equals(Material.LIGHT_WEIGHTED_PRESSURE_PLATE)) return;
        Player player = event.getPlayer();
        if (!PlayerParkourSession.isActive(player)) return;
        if (player.getLocation().getY() <= 75) return;
        PlayerParkourSession.getSession(player, false).registerEnd();
        PlayerParkourSession.remove(player);
    }

    @EventHandler
    public void cancelFly(PlayerMoveEvent event) {
        boolean active = PlayerParkourSession.isActive(event.getPlayer());
        if (active) {
            event.getPlayer().setAllowFlight(false);
            event.getPlayer().setFlying(false);
        }
    }
}
