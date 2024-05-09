package us.smartmc.game.luckytowers.instance.game;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import us.smartmc.game.luckytowers.LuckyTowers;
import us.smartmc.game.luckytowers.instance.player.GamePlayer;
import us.smartmc.game.luckytowers.instance.player.PlayerStatus;
import us.smartmc.game.luckytowers.manager.GameSessionsManager;
import us.smartmc.game.luckytowers.messages.GameMessages;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class GameSession implements IGameSession {

    private static final int DEFAULT_SECONDS_COOLDOWN = 5;

    private final UUID id;

    private final GameMap map;

    private final Set<GamePlayer> players = new HashSet<>();

    @Getter
    private final GameSessionTeams teams;

    @Setter @Getter
    private GameSessionStatus status = GameSessionStatus.WAITING;

    public GameSession(UUID id, GameMap map) {
        this.id = id;
        this.map = map;
        this.teams = new GameSessionTeams(this);
    }

    @Override
    public void start() {
        setStatus(GameSessionStatus.STARTING);

        teams.forEachTeam(team -> {
            team.getPlayers().forEach(uuid -> {
                GamePlayer gamePlayer = GamePlayer.get(uuid);
                gamePlayer.onlinePlayer(p -> {
                    p.teleport(team.getSpawnAssigned());
                });
            });
        });
        BukkitRunnable runnable = new BukkitRunnable() {
            int cooldown = DEFAULT_SECONDS_COOLDOWN;
            float soundPitch = 0.0f;
            @Override
            public void run() {
                if (cooldown <= 0) {
                    broadcastMessage(GameMessages.session_message_started);
                    setStatus(GameSessionStatus.PLAYING);
                    cancel();
                }
                if (cooldown >= 1) {
                    broadcastSound(Sound.BLOCK_GRASS_BREAK, 1f, soundPitch);
                    broadcastActionbar(GameMessages.session_actionBar_startingIn, cooldown);
                }
                cooldown--;
                soundPitch += 0.2f;
            }
        };
        runnable.runTaskTimerAsynchronously(LuckyTowers.getPlugin(), 0, 20);
    }

    @Override
    public void end() {
        if (getStatus().equals(GameSessionStatus.ENDING)) return;
        setStatus(GameSessionStatus.ENDING);
        GameSessionsManager manager = LuckyTowers.getManager(GameSessionsManager.class);
        manager.unregister(id);
    }

    @Override
    public boolean canStart() {
        return teams.getTeamsWithPlayersSize() >= map.getTemplate().getMinTeamSize();
    }

    @Override
    public boolean canEnd() {
        return teams.getTeamsWithPlayersSize() == 1;
    }

    @Override
    public void joinPlayer(GamePlayer player) {
        player.onlinePlayer(p -> p.teleport(map.getSpawn()));
        player.setGameSession(this);
        player.setStatus(PlayerStatus.INGAME);
        teams.assignNextEmptyTeam(player);
        if (canStart()) start();
    }

    @Override
    public void quitPlayer(GamePlayer player) {
        teams.clearTeams(player);
        players.remove(player);
        player.setStatus(PlayerStatus.LOBBY);
        player.setGameSession(null);
        if (canEnd()) end();
    }

    @Override
    public void deathPlayer(GamePlayer player) {
        if (canEnd()) end();
    }

    @Override
    public Collection<Player> getAlivePlayers() {
        Set<Player> players = new HashSet<>();
        teams.forEachTeam(team -> {
            team.getPlayers().forEach(id -> {
                Player player = Bukkit.getPlayer(id);
                if (player == null) return;
                players.add(player);
            });
        });
        return players;
    }
}
