package me.imsergioh.smartcorewaterfall.manager.exception;

public class RedisConnectionNotInitializedException extends Exception {
  public RedisConnectionNotInitializedException() {
    super("Redis connection not initialized");
  }
}
