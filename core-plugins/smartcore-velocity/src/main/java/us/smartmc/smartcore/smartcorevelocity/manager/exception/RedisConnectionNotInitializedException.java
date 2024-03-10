package us.smartmc.smartcore.smartcorevelocity.manager.exception;

public class RedisConnectionNotInitializedException extends Exception {
  public RedisConnectionNotInitializedException() {
    super("Redis connection not initialized");
  }
}
