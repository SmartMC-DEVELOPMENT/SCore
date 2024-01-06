package me.imsergioh.smartcorewaterfall.manager.cooldown.implementation.friend.event;

import me.imsergioh.pluginsapi.connection.RedisConnection;
import me.imsergioh.pluginsapi.instance.handler.RedisPubSubListener;
import me.imsergioh.smartcorewaterfall.manager.cooldown.implementation.friend.FriendRequestObject;
import me.imsergioh.smartcorewaterfall.manager.cooldown.implementation.friend.IFriendRequest;
import me.imsergioh.smartcorewaterfall.util.AsyncUtilities;
import redis.clients.jedis.params.GetExParams;

import java.util.UUID;

public class FriendRequestEventHandler extends RedisPubSubListener {

  public static final String KEY = "cooldown.friend.request";

  public FriendRequestEventHandler() {
    super(FriendRequestEventHandler.KEY);
  }

  @Override
  public void onMessage(String message) {
    FriendRequestEventHandler.trigger(message);
  }

  protected static void trigger(IFriendRequest object, boolean async) {
    if (async) {
      AsyncUtilities.schedule(object::requestFriendIfConnected);
    } else object.requestFriendIfConnected();
  }

  public static void trigger(UUID senderUUID, UUID receiverUUID, boolean async) {
    trigger(new FriendRequestObject(senderUUID, receiverUUID), async);
  }

  protected static void trigger(String data) {
    String path = "cooldown." + data;
    String string = RedisConnection.mainConnection.getResource().get(path);
    if (string.isEmpty()) {
      return;
    }

    trigger(FriendRequestObject.fromString(
            data.replaceFirst("friend", string)
    ), true);
  }
}
