package me.imsergioh.smartcorewaterfall.manager.cooldown.implementation.friend;

import lombok.Getter;

public enum FriendCooldownStatus {
  ACCEPTED,
  REMOVED,
  PENDING,
  DENIED;

  @Getter
  private final boolean notify;

  FriendCooldownStatus() {
    this.notify = false;
  }

  FriendCooldownStatus(boolean notify) {
    this.notify = notify;
  }
}
