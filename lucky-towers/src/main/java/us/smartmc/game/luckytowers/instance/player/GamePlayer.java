package us.smartmc.game.luckytowers.instance.player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.EnumWrappers;
import lombok.Getter;
import lombok.Setter;
import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.language.IMessageCategory;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.util.BukkitChatUtil;
import me.imsergioh.pluginsapi.util.ChatUtil;
import net.md_5.bungee.api.chat.BaseComponent;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import us.smartmc.game.luckytowers.LuckyTowers;
import us.smartmc.game.luckytowers.event.player.GamePlayerWinEvent;
import us.smartmc.game.luckytowers.event.player.PlayerStatusChangeEvent;
import us.smartmc.game.luckytowers.instance.game.GameSession;
import us.smartmc.game.luckytowers.manager.PlayersManager;
import us.smartmc.game.luckytowers.messages.GameMessages;

import java.time.Duration;
import java.util.UUID;
import java.util.function.Consumer;

public class GamePlayer {

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

    @Setter
    @Getter
    private GameSession gameSession;

    @Getter
    private PlayerStatus status = PlayerStatus.LOBBY;

    private GamePlayer(UUID uuid) {
        this.uuid = uuid;
        this.data = loadData(uuid);
        this.bukkitPlayer = Bukkit.getPlayer(uuid);
        setStatus(PlayerStatus.LOBBY);
    }

    public void resetTitle() {
        getBukkitPlayer().resetTitle();
    }

    public void sendTitle(IMessageCategory titleCategory, IMessageCategory subtitleCategory, Object... args) {
        sendTitle(titleCategory, subtitleCategory, 0, 30, 0, args);
    }

    public void sendTitle(IMessageCategory titleCategory, IMessageCategory subtitleCategory, int fadeIn, int stay, int fadeOut, Object... args) {
        String title = BukkitChatUtil.parse(getBukkitPlayer(), titleCategory, args).toLegacyText();
        String subtitle = BukkitChatUtil.parse(getBukkitPlayer(), subtitleCategory, args).toLegacyText();

        // Construct the packet for the title text
        PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(
                PacketPlayOutTitle.EnumTitleAction.TITLE, new ChatMessage(title), fadeIn, stay, fadeOut);
        // Construct the packet for the subtitle text
        PacketPlayOutTitle subtitlePacket = new PacketPlayOutTitle(
                PacketPlayOutTitle.EnumTitleAction.SUBTITLE, new ChatMessage(subtitle), fadeIn, stay, fadeOut);

        // Send the packets to the player
        ((CraftPlayer) getBukkitPlayer()).getHandle().playerConnection.sendPacket(titlePacket);
        ((CraftPlayer) getBukkitPlayer()).getHandle().playerConnection.sendPacket(subtitlePacket);
    }

    public void addCoins(int amount) {
        addCoins(amount, null);
    }

    public void addCoins(int amount, IMessageCategory reason) {
        data.addToNumber(COINS_KEY, amount);
        onlinePlayer(player -> {
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.5f);
            Language language = PlayerLanguages.get(player.getUniqueId());

            String component = BukkitChatUtil.parse(player, GameMessages.player_addedCoins.getMessageOf(language), amount).toLegacyText();
            sendActionBar(player, component);
        });

        IMessageCategory message = reason == null ? GameMessages.addedCoins_withoutReason : GameMessages.addedCoins_withReason;

        if (reason != null) {
            sendMessage(message, amount, reason.getMessageOf(PlayerLanguages.get(uuid)));
        } else {
            sendMessage(message, amount);
        }
    }

    public void addGamePlayed() {
        data.increaseNumber(GAMES_PLAYED_KEY);
    }

    public void addWin() {
        data.increaseNumber(WINS_KEY);
        data.increaseStreak(WINS_KEY);
        Bukkit.getPluginManager().callEvent(new GamePlayerWinEvent(this));
        addCoins(10, GameMessages.coinsReason_win);
        sendTitle(GameMessages.win_title, GameMessages.win_subtitle);
    }

    public void addKill(Location killLocation) {
        data.increaseNumber(KILLS_KEY);
        data.increaseStreak(KILLS_KEY);
        onlinePlayer(player -> {
            player.playSound(killLocation, Sound.VILLAGER_HIT, 1.0f, -1.5f);
            Language language = PlayerLanguages.get(player.getUniqueId());

            BaseComponent component = BukkitChatUtil.parse(player, GameMessages.player_addedKill.getMessageOf(language));
            sendActionBar(player, component.toLegacyText());
        });
        addCoins(4, GameMessages.coinsReason_kill);
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

    public void sendMessage(IMessageCategory messageCategory, Object... args) {
        BukkitChatUtil.send(getBukkitPlayer(), messageCategory, args);
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
        return manager.get(uuid);
    }

    public static GamePlayer create(UUID uuid) {
        return new GamePlayer(uuid);
    }

    public static void sendActionBar(Player player, String message) {
        IChatBaseComponent chat = new ChatComponentText(message);
        PacketPlayOutChat packet = new PacketPlayOutChat(chat, (byte) 2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

}
