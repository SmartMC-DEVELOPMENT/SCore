package us.smartmc.game.luckytowers.instance.player;

import lombok.Getter;
import lombok.Setter;
import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.util.PaperChatUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import us.smartmc.game.luckytowers.LuckyTowers;
import us.smartmc.game.luckytowers.event.player.GamePlayerWinEvent;
import us.smartmc.game.luckytowers.event.player.PlayerStatusChangeEvent;
import us.smartmc.game.luckytowers.instance.game.GameSession;
import us.smartmc.game.luckytowers.manager.PlayersManager;
import us.smartmc.game.luckytowers.menu.LobbyHotbar;
import us.smartmc.game.luckytowers.messages.GameMessages;

import java.util.UUID;
import java.util.function.Consumer;

public class GamePlayer {

    private static final LuckyTowers plugin = LuckyTowers.getPlugin();
    private static final PlayersManager manager = LuckyTowers.getManager(PlayersManager.class);

    public static final String WINS_KEY = "wins";
    public static final String KILLS_KEY = "kills";
    public static final String DEATHS_KEY = "deaths";
    public static final String COINS_KEY = "coins";
    public static final String GAMES_PLAYED_KEY = "games_played";

    @Getter
    private final UUID uuid;
    private Player bukkitPlayer;

    @Getter
    private final GamePlayerData data;

    @Setter @Getter
    private GameSession gameSession;

    @Getter
    private PlayerStatus status = PlayerStatus.LOBBY;

    private GamePlayer(UUID uuid) {
         this.uuid = uuid;
         this.data = loadData(uuid);
         this.bukkitPlayer = Bukkit.getPlayer(uuid);
         setStatus(PlayerStatus.LOBBY);

        Bukkit.getScheduler().runTask(plugin, () ->  {
            onlinePlayer(player -> {
                new LobbyHotbar(player).set(player);
            });
        });
    }

    public long getGamesPlayed() {
        return data.getBigNumber(GAMES_PLAYED_KEY);
    }

    public long getDeaths() {
        return data.getBigNumber(DEATHS_KEY);
    }

    public long getKills() {
        return data.getBigNumber(KILLS_KEY);
    }

    public long getWins() {
        return data.getBigNumber(WINS_KEY);
    }

    public long getCoins() {
        return data.getBigNumber(COINS_KEY);
    }

    public void addCoins(int amount) {
        data.addToNumber(COINS_KEY, amount);
        onlinePlayer(player -> {
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f);
            Language language = PlayerLanguages.get(player.getUniqueId());

            Component component = PaperChatUtil.parse(player, GameMessages.player_addedCoins.getMessageOf(language), amount);
            player.sendActionBar(component);
        });
    }

    public void addGamePlayed() {
        data.increaseNumber(GAMES_PLAYED_KEY);
    }

    public void addWin() {
        data.increaseNumber(WINS_KEY);
        data.increaseStreak(WINS_KEY);
        Bukkit.getPluginManager().callEvent(new GamePlayerWinEvent(this));
    }

    public void addKill(Location killLocation) {
        data.increaseNumber(KILLS_KEY);
        data.increaseStreak(KILLS_KEY);
        onlinePlayer(player -> {
            player.playSound(killLocation, Sound.BLOCK_NYLIUM_HIT, 1.0f, -1.5f);
            Language language = PlayerLanguages.get(player.getUniqueId());

            Component component = PaperChatUtil.parse(player, GameMessages.player_addedKill.getMessageOf(language));
            player.sendActionBar(component);
        });
        addCoins(4);
    }

    public void addDeath() {
        data.increaseNumber(DEATHS_KEY);
        data.resetStreak(KILLS_KEY);
        data.resetStreak(WINS_KEY);
    }

    public void unload() {
        data.save();
    }

    public void setStatus(PlayerStatus status) {
        PlayerStatusChangeEvent event = new PlayerStatusChangeEvent(this, status, this.status);
        LuckyTowers.callEvent(event);
        if (event.isCancelled()) return;
        this.status = status;
    }

    public void onlinePlayer(Consumer<Player> consumer) {
        if (bukkitPlayer == null) bukkitPlayer = getBukkitPlayer();
        if (bukkitPlayer == null) return;
        if (!bukkitPlayer.isOnline()) return;
        consumer.accept(bukkitPlayer);
    }

    public Player getBukkitPlayer() {
        if (bukkitPlayer == null) {
            bukkitPlayer = Bukkit.getPlayer(uuid);
        }
        return bukkitPlayer;
    }

    private static GamePlayerData loadData(UUID uuid) {
        return new GamePlayerData(uuid);
    }

    public static GamePlayer get(UUID uuid) {
        GamePlayer gamePlayer = manager.get(uuid);
        if (gamePlayer == null) {
            gamePlayer = new GamePlayer(uuid);
            manager.register(uuid, gamePlayer);
        }
        return gamePlayer;
    }

}
