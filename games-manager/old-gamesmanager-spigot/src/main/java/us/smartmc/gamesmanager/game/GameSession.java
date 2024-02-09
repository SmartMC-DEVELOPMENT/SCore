
package us.smartmc.gamesmanager.game;

import lombok.Getter;
import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.smartmc.gamesmanager.game.map.GameMap;
import us.smartmc.gamesmanager.game.map.GameMapSession;
import us.smartmc.gamesmanager.game.task.GameStartTask;
import us.smartmc.gamesmanager.manager.GameSessionManager;
import us.smartmc.gamesmanager.message.GameMessages;
import us.smartmc.gamesmanager.player.GamePlayer;
import us.smartmc.gamesmanager.player.PlayerStatus;
import us.smartmc.gamesmanager.util.LogUtils;
import us.smartmc.gamesmanager.util.WorldUtils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;


public class GameSession<P extends Player> implements IGameSession<P> {

    private final UUID sessionID;
    protected final GamePreset gameInstance;
    @Getter
    protected GameStatus status = GameStatus.WAITING;

    protected final Set<Player> players = new HashSet<>();

    protected GameMap map;
    protected GameMapSession mapSession;


    public GameSession(GamePreset instance) {
        this.sessionID = UUID.randomUUID();
        this.gameInstance = instance;
        GameSessionManager.register(this);
    }

    @Override
    public void unload() {
        LogUtils.log(getClass(), sessionID + " unloading...");
        end();
        LogUtils.log(getClass(), sessionID + " unloaded correctly!");
    }

    @Override
    public void start() {
        setStatus(GameStatus.PLAYING);
    }


    @Override
    public void end() {
        setStatus(GameStatus.ENDING);
        // TODO: DO STUFF HERE TO END GAME ->
        WorldUtils.deleteTempWorld(mapSession.getWorld());
    }

    // This method will automatically set a random map from the whitelisted map list when it gets invoked by the first time
    private void selectRandomWhitelistedMap() {
        if (map != null) return;
        String mapName = gameInstance.getMapWhitelist().get(new Random().nextInt(gameInstance.getMapWhitelist().size()));
        map = GameMap.get(mapName);
        mapSession = new GameMapSession(map);
    }

    @Override
    public void joinPlayer(P player) {
        selectRandomWhitelistedMap();
        if (!canPlayerJoin(player)) return;
        players.add(player);
        broadcast(GameMessages.getMessageVariable("player_joined"), player.getPlayer().getName(),
                players.size(), map.getMaxPlayers());
        checkStart();
    }

    @Override
    public void quitPlayer(P player) {
    }

    @Override
    public void deathPlayer(P player) {
        String title = ChatUtil.parse(player.getPlayer(), GameMessages.getMessageVariable("death_title"));
        String subtitle = ChatUtil.parse(player.getPlayer(), GameMessages.getMessageVariable("death_subtitle"));
        player.getPlayer().sendTitle(title, subtitle);
    }

    @Override
    public void checkStart() {
        System.out.println("Checking start " + players.size() + " " + map.getMinPlayers());
        if (players.size() < map.getMinPlayers()) return;
        new GameStartTask((IGameSession<Player>) this, 3);
    }

    @Override
    public boolean canPlayerJoin(P player) {
        GamePlayer gamePlayer = GamePlayer.get(player);
        if (!((gamePlayer.getStatus().equals(PlayerStatus.LOBBY) || !gamePlayer.getStatus().equals(PlayerStatus.SPECTATING)))) return false;

        // If it is starting and don't exceed player max amount: true
        return (status.equals(GameStatus.STARTING) || status.equals(GameStatus.WAITING)) &&
                players.size() < map.getMaxPlayers();
    }

    @Override
    public boolean canSpectatorJoin(P player) {
        return getStatus().equals(GameStatus.STARTING);
    }

    @Override
    public boolean canStart() {
        return getStatus().equals(GameStatus.WAITING);
    }

    @Override
    public void broadcast(String message, Object... args) {
        forEachPlayer(player -> {
            try {
                GamePlayer.get(player).sendMessage(message, args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void forEachPlayer(Consumer<P> consumer) {
        players.forEach(player ->  {
            consumer.accept((P) player);
        });
    }

    @Override
    public void setStatus(GameStatus status) {
        this.status = status;
    }

    @Override
    public UUID getID() {
        return sessionID;
    }
}
