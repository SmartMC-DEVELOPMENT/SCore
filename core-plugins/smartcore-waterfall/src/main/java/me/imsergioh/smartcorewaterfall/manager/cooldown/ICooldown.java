package me.imsergioh.smartcorewaterfall.manager.cooldown;

public interface ICooldown {

  long getTimestamp();

  void setTimestamp(long timestamp);

  long getDuration();

  String getDataDirectory();

  String getIdentification();

  void schedule();
}
