package me.imsergioh.smartcorewaterfall.manager.cooldown;

import lombok.Getter;
import lombok.Setter;
import me.imsergioh.smartcorewaterfall.manager.cooldown.exception.CooldownAlreadyFinishedException;
import me.imsergioh.smartcorewaterfall.manager.exception.RedisConnectionNotInitializedException;

@Getter
@Setter
public class CooldownImplementation implements ICooldown {

  private final String dataDirectory;
  private final long duration;

  private long timestamp;
  private String identification = "";

  public CooldownImplementation(long timestamp, long duration, String dataDirectory) {
    this.timestamp = timestamp;
    this.duration = duration;
    this.dataDirectory = dataDirectory;
  }

  @Override
  public void schedule() {
    try {
      CooldownManager.registerCooldown(this);
    } catch (
            RedisConnectionNotInitializedException
            | CooldownAlreadyFinishedException e) {
      e.printStackTrace(System.out);
    }
  }
}
