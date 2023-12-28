package me.imsergioh.smartcorewaterfall.manager.cooldown.implementation.friend.event;

import com.google.common.base.Charsets;
import me.imsergioh.pluginsapi.connection.RedisConnection;
import me.imsergioh.pluginsapi.instance.handler.RedisPubSubListener;
import me.imsergioh.smartcorewaterfall.manager.cooldown.implementation.friend.FriendRequestObject;
import me.imsergioh.smartcorewaterfall.manager.cooldown.implementation.friend.IFriendRequest;
import me.imsergioh.smartcorewaterfall.util.AsyncUtilities;

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
    final RedisConnection redisConnection = RedisConnection.mainConnection;
    if (redisConnection == null) {
      return;
    }
    final byte[] dataResource = redisConnection.getResource().get(data.getBytes(Charsets.UTF_8));
    if (dataResource.length < 1) {
      return;
    }

    final byte status = dataResource[0];
    trigger(FriendRequestObject.fromString(
            data.replaceFirst("friend", Byte.toString(status))
    ), true);
  }
}
