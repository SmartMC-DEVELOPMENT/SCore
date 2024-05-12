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
import us.smartmc.game.luckytowers.event.player.PlayerStatusChangeEvent;
import us.smartmc.game.luckytowers.instance.game.GameSession;
import us.smartmc.game.luckytowers.manager.PlayersManager;
import us.smartmc.game.luckytowers.messages.GameMessages;

import java.util.UUID;
import java.util.function.Consumer;

public class GamePlayer {

    private static final PlayersManager manager = LuckyTowers.getManager(PlayersManager.class);

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
    }

    public void addCoins(int amount) {
        data.addToInt("coins", amount);
        onlinePlayer(player -> {
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f);
            Language language = PlayerLanguages.get(player.getUniqueId());

            Component component = PaperChatUtil.parse(player, GameMessages.player_addedCoins.getMessageOf(language), amount);
            player.sendActionBar(component);
        });
    }

    public void addKill(Location killLocation) {
        data.increaseNumber("kills");
        onlinePlayer(player -> {
            player.playSound(killLocation, Sound.BLOCK_NYLIUM_HIT, 1.0f, -1.5f);
            Language language = PlayerLanguages.get(player.getUniqueId());

            Component component = PaperChatUtil.parse(player, GameMessages.player_addedKill.getMessageOf(language));
            player.sendActionBar(component);
        });
        addCoins(4);
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
