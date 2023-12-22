package us.smartmc.gamesmanager.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class GamePlayerRepository {

  protected static final Map<UUID, Object> gamePlayers = new HashMap<>();

  public static <T extends GamePlayer> T provide(Class<T> playerType, Player player) {
    final Object gamePlayer = gamePlayers.computeIfAbsent(player.getUniqueId(), u -> {
      try {
        final Constructor<T> constructor = playerType.getDeclaredConstructor(Player.class);
        constructor.setAccessible(true);
        return constructor.newInstance(player);
      } catch (Exception e) {
        e.printStackTrace(System.out);
      }
      return null;
    });
    if (gamePlayer == null || !gamePlayer.getClass().isAssignableFrom(playerType)) {
      gamePlayers.remove(player.getUniqueId());
      return null;
    }
    return playerType.cast(gamePlayer);
  }

  public static <T extends GamePlayer> T provide(Class<T> playerType, UUID uuid) {
    return provide(playerType, Bukkit.getPlayer(uuid));
  }

  public static boolean remove(UUID uuid) {
    return gamePlayers.remove(uuid) != null;
  }
}
