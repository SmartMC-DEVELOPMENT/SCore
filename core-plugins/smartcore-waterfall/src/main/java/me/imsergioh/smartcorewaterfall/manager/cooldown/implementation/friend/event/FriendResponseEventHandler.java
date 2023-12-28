package me.imsergioh.smartcorewaterfall.manager.cooldown.implementation.friend.event;

import me.imsergioh.pluginsapi.instance.handler.RedisPubSubListener;
import me.imsergioh.smartcorewaterfall.manager.cooldown.implementation.friend.FriendCooldownStatus;
import me.imsergioh.smartcorewaterfall.manager.cooldown.implementation.friend.FriendRequestObject;
import me.imsergioh.smartcorewaterfall.manager.cooldown.implementation.friend.IFriendRequest;
import me.imsergioh.smartcorewaterfall.util.AsyncUtilities;

import java.util.UUID;

public class FriendResponseEventHandler extends RedisPubSubListener {

  public static final String KEY = "cooldown.friend.response";

  public FriendResponseEventHandler() {
    super(FriendResponseEventHandler.KEY);
  }

  @Override
  public void onMessage(String message) {
    FriendResponseEventHandler.trigger(message);
  }

  protected static void trigger(IFriendRequest object, boolean async) {
    if (async) {
      AsyncUtilities.schedule(object::answerFriendRequest);
    } else object.answerFriendRequest();
  }

  public static void trigger(UUID senderUUID, UUID receiverUUID, FriendCooldownStatus cooldownStatus, boolean async) {
    final IFriendRequest friendRequest = new FriendRequestObject(senderUUID, receiverUUID);
    friendRequest.setStatus(cooldownStatus);
    trigger(friendRequest, async);
  }

  protected static void trigger(String data) {
    trigger(FriendRequestObject.fromString(data), true);
  }
}
