package me.imsergioh.smartcorewaterfall.manager.cooldown.implementation.friend;

import java.util.UUID;

public interface IFriendRequest {

  UUID getSenderUUID();

  UUID getReceiverUUID();

  FriendCooldownStatus getStatus();

  void setStatus(FriendCooldownStatus status);

  boolean requestFriendIfConnected();

  boolean answerFriendRequest();
}
