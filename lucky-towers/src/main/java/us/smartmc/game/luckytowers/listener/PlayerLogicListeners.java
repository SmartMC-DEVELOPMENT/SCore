package us.smartmc.game.luckytowers.listener;

import me.imsergioh.pluginsapi.event.PlayerDataLoadedEvent;
import me.imsergioh.pluginsapi.event.PlayerLanguageChangedEvent;
import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.util.ChatUtil;
import me.imsergioh.pluginsapi.util.SyncUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import us.smartmc.core.SmartCore;
import us.smartmc.game.luckytowers.LuckyTowers;
import us.smartmc.game.luckytowers.config.MainPluginConfig;
import us.smartmc.game.luckytowers.event.GameStatusChangeEvent;
import us.smartmc.game.luckytowers.event.player.GamePlayerDeathEvent;
import us.smartmc.game.luckytowers.event.player.GamePlayerJoinSessionEvent;
import us.smartmc.game.luckytowers.event.player.PlayerStatusChangeEvent;
import us.smartmc.game.luckytowers.instance.game.GameMap;
import us.smartmc.game.luckytowers.instance.game.GameSession;
import us.smartmc.game.luckytowers.instance.game.GameSessionStatus;
import us.smartmc.game.luckytowers.instance.player.GamePlayer;
import us.smartmc.game.luckytowers.instance.player.PlayerStatus;
import us.smartmc.game.luckytowers.manager.EditorModeManager;
import us.smartmc.game.luckytowers.manager.GameMapManager;
import us.smartmc.game.luckytowers.manager.GameSessionsManager;
import us.smartmc.game.luckytowers.manager.PlayersManager;
import us.smartmc.game.luckytowers.menu.LobbyHotbar;
import us.smartmc.game.luckytowers.menu.hotbar.SpectatorHotbar;
import us.smartmc.game.luckytowers.menu.hotbar.WaitingHotbar;
import us.smartmc.game.luckytowers.messages.GameMessages;
import us.smartmc.game.luckytowers.util.GameUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayerLogicListeners implements Listener {

    @EventHandler
    public void clearArmorContent(PlayerStatusChangeEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;
        player.getInventory().setArmorContents(null);
    }

    @EventHandler
    public void reloadLanguageInventory(PlayerLanguageChangedEvent event) {
        CorePlayer corePlayer = event.getCorePlayer();

        CoreMenu menu = corePlayer.getCurrentMenuSet();
        if (menu == null) return;

        GamePlayer gamePlayer = GamePlayer.get(corePlayer.getUUID());
        if (gamePlayer.getGameSession() != null && gamePlayer.getGameSession().getStatus().equals(GameSessionStatus.PLAYING)) return;

        menu.load();
        menu.set(corePlayer.getBukkitPlayer());
    }

    @EventHandler
    public void updateVisiblityOnStatusChangeEvent(PlayerStatusChangeEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;
        GameUtil.updateVisibility(event.getPlayer());
    }

    @EventHandler
    public void flyHandle(PlayerStatusChangeEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;

        if (event.getStatus().equals(PlayerStatus.SPECTATING)) {
            player.setAllowFlight(true);
            return;
        }
        player.setAllowFlight(false);
    }

    @EventHandler
    public void cancelMoveAtStartGame(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
        if (gamePlayer == null) return;
        GameSession session = gamePlayer.getGameSession();
        if (session == null) return;
        if (!session.isStartedRecently()) return;

        Location from = event.getFrom();
        Location to = event.getTo();

        if (from.getBlockX() != to.getBlockX()) {
            event.setCancelled(true);
            event.getPlayer().teleport(new Location(from.getWorld(), from.getBlockX(), from.getY(), from.getBlockZ() + 0.5));
            player.sendTitle("", ChatUtil.parse(player, GameMessages.title_dontFall.getMessageOf(PlayerLanguages.get(player.getUniqueId()))));

        }

        if (from.getBlockZ() != to.getBlockZ()) {
            event.setCancelled(true);
            event.getPlayer().teleport(new Location(from.getWorld(), from.getBlockX(), from.getY(), from.getBlockZ() + 0.5));
            player.sendTitle("", ChatUtil.parse(player, GameMessages.title_dontFall.getMessageOf(PlayerLanguages.get(player.getUniqueId()))));
        }
    }

    @EventHandler
    public void setGamemodeCreativeAtEditorTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        EditorModeManager manager = LuckyTowers.getManager(EditorModeManager.class);
        if (!manager.hasEditorMode(player)) return;
        player.setGameMode(GameMode.CREATIVE);
    }

    @EventHandler
    public void joinGameAtPortal(PlayerPortalEvent event) {
        boolean isEndPortal = event.getTo().getWorld().getName().contains("_the_end");
        if (!isEndPortal) return;
        event.setCancelled(true);

        GamePlayer gamePlayer = GamePlayer.get(event.getPlayer().getUniqueId());
        if (gamePlayer == null) return;

        GameSessionsManager sessionsManager = LuckyTowers.getManager(GameSessionsManager.class);
        List<GameSession> prioSessions = sessionsManager.getPrioSessions();

        if (prioSessions.isEmpty()) {
            pickRandomMapToJoin(event.getPlayer());
            return;
        }
        prioSessions.get(0).joinPlayer(gamePlayer);
    }

    @EventHandler
    public void cancelInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());

        if (SmartCore.getPlugin().getAdminModeHandler().isActive(player)) return;
        if (gamePlayer.getStatus().equals(PlayerStatus.INGAME)) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void createGamePlayer(PlayerDataLoadedEvent event) {
        GamePlayer.create(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void giveLobbyHotbar(PlayerStatusChangeEvent event) {
        if (!event.getStatus().equals(PlayerStatus.LOBBY)) return;
        Player player = event.getPlayer();
        if (player == null) return;
        Bukkit.getScheduler().runTask(LuckyTowers.getPlugin(), () -> {
            if (!player.isOnline()) return;
            new LobbyHotbar(player).set(player);
        });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void unloadGamePlayer(PlayerQuitEvent event) {
        PlayersManager manager = LuckyTowers.getManager(PlayersManager.class);
        manager.unregister(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void addDeath(GamePlayerDeathEvent event) {
        event.getGamePlayer().addDeath();
    }

    @EventHandler
    public void addGamePlayed(PlayerStatusChangeEvent event) {
        if (event.getStatus().equals(PlayerStatus.INGAME)) event.getGamePlayer().addGamePlayed();
    }

    @EventHandler
    public void teleportToLobby(PlayerStatusChangeEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getStatus().equals(PlayerStatus.LOBBY) && MainPluginConfig.isLobbyEnabled())) return;

        // Teleport to lobby if gameplayer is online (Should xd)
        event.getGamePlayer().onlinePlayer(player -> player.teleport(MainPluginConfig.getLobby()));
    }

    @EventHandler
    public void updateScoreboard(PlayerStatusChangeEvent event) {
        if (event.getPlayer() == null) return;
        if (!event.getPlayer().isOnline()) return;
        GameUtil.updateScoreboard(event.getGamePlayer());
    }

    @EventHandler
    public void clearInventory(GameStatusChangeEvent event) {
        GameSession session = event.getSession();
        if (session == null) return;
        if (!(session.getStatus().equals(GameSessionStatus.STARTING))) return;
        session.forEachOnlinePlayer(p -> p.getInventory().setArmorContents(null));
    }

    @EventHandler
    public void giveWaitingHotbar(GamePlayerJoinSessionEvent event) {
        new WaitingHotbar(event.getPlayer()).set(event.getPlayer());
    }

    @EventHandler
    public void giveSpectatorHotbar(PlayerStatusChangeEvent event) {
        if (!(event.getStatus().equals(PlayerStatus.SPECTATING))) return;
        Bukkit.getScheduler().runTask(LuckyTowers.getPlugin(), () -> {
            if (!event.getPlayer().isOnline()) return;
            if (GamePlayer.get(event.getPlayer().getUniqueId()).getGameSession() == null) return;
            new SpectatorHotbar(event.getPlayer()).set(event.getPlayer());
        });
    }

    public static void pickRandomMapToJoin(Player player) {
        GameMapManager gameMapManager = LuckyTowers.getManager(GameMapManager.class);
        GameSessionsManager sessionsManager = LuckyTowers.getManager(GameSessionsManager.class);

        List<String> mapNames = new ArrayList<>();

        for (GameMap map : gameMapManager.values()) {
            mapNames.add(map.getName());
        }
        String randomMap = mapNames.get(new Random().nextInt(mapNames.size()));
        GameSession session = sessionsManager.createOrGetByName(randomMap, 1);
        session.joinPlayer(GamePlayer.get(player.getUniqueId()));
    }

}
