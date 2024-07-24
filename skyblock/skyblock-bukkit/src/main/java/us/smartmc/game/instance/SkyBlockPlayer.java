package us.smartmc.game.instance;

import lombok.Getter;
import lombok.Setter;
import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.smartmc.core.instance.player.SmartCorePlayer;
import us.smartmc.game.SkyBlockPlugin;
import us.smartmc.game.message.SkyBlockPlayerMesssages;
import us.smartmc.skyblock.instance.player.ISkyBlockPlayer;
import us.smartmc.skyblock.manager.PlayersManager;

import java.util.UUID;

@Getter
public class SkyBlockPlayer implements ISkyBlockPlayer {

    private final UUID id;

    private Player bukkitPlayer;
    private SmartCorePlayer corePlayer;

    private SkyBlockPlayerData playerData;

    @Setter
    private long joinedAt = -1;

    protected SkyBlockPlayer(UUID id) {
        this.id = id;
        PlayersManager.register(this);
        load();
    }

    public void addCoins(long amount) {
        playerData.addCoins(amount);
        PaperChatUtil.send(getBukkitPlayer(), SkyBlockPlayerMesssages.coinsAdded, amount);
    }

    @Override
    public void load() {
        playerData = new SkyBlockPlayerData(this);
    }

    @Override
    public void register() {

    }

    @Override
    public void unregister() {
        // Register & calcular played time when leaving
        if (joinedAt != -1)
            playerData.registerEndPlayTime(joinedAt);
        playerData.saveData();
    }

    public SmartCorePlayer getCorePlayer() {
        if (corePlayer == null) {
            corePlayer = SmartCorePlayer.get(id);
        }
        return corePlayer;
    }

    public Player getBukkitPlayer() {
        if (bukkitPlayer == null) {
            bukkitPlayer = Bukkit.getPlayer(id);
        }
        return bukkitPlayer;
    }

    public static SkyBlockPlayer getOrCreate(Player player) {
        UUID id = player.getUniqueId();
        ISkyBlockPlayer skyblockPlayer = PlayersManager.get(id);
        return skyblockPlayer != null ? (SkyBlockPlayer) skyblockPlayer : new SkyBlockPlayer(id);
    }

    public static SkyBlockPlayer get(Player player) {
        return get(player.getUniqueId());
    }

    public static SkyBlockPlayer get(UUID id) {
        return (SkyBlockPlayer) PlayersManager.get(id);
    }

}
