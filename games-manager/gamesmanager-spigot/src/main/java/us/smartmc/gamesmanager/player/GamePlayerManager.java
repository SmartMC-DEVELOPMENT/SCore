package us.smartmc.gamesmanager.player;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class GamePlayerManager {

  protected static final Map<UUID, GamePlayer> gamePlayers = new HashMap<>();

  public static GamePlayer get(Player player) {
    return get(player.getUniqueId());
  }

  public static GamePlayer get(UUID uuid) {
    return gamePlayers.get(uuid);
  }

  public static void register(GamePlayer player) {
    gamePlayers.put(player.getUuid(), player);
  }

  public static boolean remove(UUID uuid) {
    return gamePlayers.remove(uuid) != null;
  }
}
