package us.smartmc.smartcore.proxy.manager.exception;

public class RedisConnectionNotInitializedException extends Exception {
  public RedisConnectionNotInitializedException() {
    super("Redis connection not initialized");
  }
}
