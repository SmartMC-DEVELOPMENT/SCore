package me.imsergioh.smartcorewaterfall.manager.cooldown;

import me.imsergioh.pluginsapi.connection.RedisConnection;
import me.imsergioh.smartcorewaterfall.manager.cooldown.exception.CooldownAlreadyFinishedException;
import me.imsergioh.smartcorewaterfall.manager.exception.RedisConnectionNotInitializedException;

import java.util.Set;
import java.util.stream.Collectors;

public class CooldownManager {

  public static String registerCooldown(ICooldown cooldown) throws
          RedisConnectionNotInitializedException,
          CooldownAlreadyFinishedException {
    final RedisConnection redisConnection = RedisConnection.mainConnection;
    if (redisConnection == null) {
      throw new RedisConnectionNotInitializedException();
    }

    final String key = "cooldown.%s".formatted(cooldown.getDataDirectory());
    final String value = cooldown.getIdentification();

    final long endMillis = cooldown.getTimestamp() + cooldown.getDuration();
    final long remainingMillis = endMillis - System.currentTimeMillis();

    if (remainingMillis <= 0) {
      throw new CooldownAlreadyFinishedException(cooldown.getDataDirectory());
    }
    return redisConnection.getResource().psetex(key, remainingMillis, value);
  }

  public static long stopCooldowns(String dataPattern) throws
          RedisConnectionNotInitializedException {
    final RedisConnection redisConnection = RedisConnection.mainConnection;
    final Set<String> keys = CooldownManager.getActiveCooldowns(dataPattern);

    long result = 1L;
    for (String key : keys) {
      final long requestResult = redisConnection.getResource().del("cooldown.%s".formatted(key));
      if (result == 1L && requestResult <= 0) {
        result = 0L;
      }
    }
    return result;
  }

  public static boolean hasActiveCooldown(String dataDirectory) throws
          RedisConnectionNotInitializedException {
    final RedisConnection redisConnection = RedisConnection.mainConnection;
    if (redisConnection == null) {
      throw new RedisConnectionNotInitializedException();
    }
    return redisConnection.getResource().exists("cooldown.%s".formatted(dataDirectory));
  }

  public static boolean hasSimilarActiveCooldown(String dataDirectory) throws
          RedisConnectionNotInitializedException {
    final RedisConnection redisConnection = RedisConnection.mainConnection;
    if (redisConnection == null) {
      throw new RedisConnectionNotInitializedException();
    }

    return !redisConnection.getResource().keys("cooldown.%s".formatted(dataDirectory)).isEmpty();
  }

  public static Set<String> getActiveCooldowns(String dataPattern) throws
          RedisConnectionNotInitializedException {
    final RedisConnection redisConnection = RedisConnection.mainConnection;
    if (redisConnection == null) {
      throw new RedisConnectionNotInitializedException();
    }

    return redisConnection.getResource().keys("cooldown.%s".formatted(dataPattern))
            .stream().map(String::new)
            .collect(Collectors.toSet());
  }
}
