package me.imsergioh.smartcorewaterfall.manager.cooldown.exception;

public class CooldownAlreadyFinishedException extends Exception {
  public CooldownAlreadyFinishedException(String dataDirectory) {
    super("Cooldown with data directory '%s' has already finished.".formatted(dataDirectory));
  }
}
