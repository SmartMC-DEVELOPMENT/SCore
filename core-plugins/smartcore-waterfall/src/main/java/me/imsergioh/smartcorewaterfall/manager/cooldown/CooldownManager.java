package me.imsergioh.smartcorewaterfall.manager.cooldown;

import com.google.common.base.Charsets;
import me.imsergioh.pluginsapi.connection.RedisConnection;
import me.imsergioh.smartcorewaterfall.manager.cooldown.exception.CooldownAlreadyFinishedException;
import me.imsergioh.smartcorewaterfall.manager.exception.RedisConnectionNotInitializedException;

import java.nio.charset.StandardCharsets;
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

    final byte[] key = "cooldown.%s"
            .formatted(cooldown.getDataDirectory())
            .getBytes(Charsets.UTF_8);
    final byte[] value = cooldown.getIdentification();

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
      final long requestResult = redisConnection.getResource().del(key.getBytes(StandardCharsets.UTF_8));

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

    return redisConnection.getResource().exists("cooldown.%s"
            .formatted(dataDirectory)
            .getBytes(Charsets.UTF_8));
  }

  public static boolean hasSimilarActiveCooldown(String dataDirectory) throws
          RedisConnectionNotInitializedException {
    final RedisConnection redisConnection = RedisConnection.mainConnection;
    if (redisConnection == null) {
      throw new RedisConnectionNotInitializedException();
    }

    return !redisConnection.getResource().keys("cooldown.%s"
            .formatted(dataDirectory)
            .getBytes(Charsets.UTF_8)
    ).isEmpty();
  }

  public static Set<String> getActiveCooldowns(String dataPattern) throws
          RedisConnectionNotInitializedException {
    final RedisConnection redisConnection = RedisConnection.mainConnection;
    if (redisConnection == null) {
      throw new RedisConnectionNotInitializedException();
    }

    return redisConnection.getResource().keys("cooldown.%s"
            .formatted(dataPattern)
            .getBytes(Charsets.UTF_8)
    ).stream().map(String::new).collect(Collectors.toSet());
  }
}
