package me.imsergioh.smartcorewaterfall.manager.cooldown.implementation.friend.event;

import me.imsergioh.pluginsapi.handler.PubSubConnectionHandler;

public class FriendEventManagement {

  public static void registerEvents() {
    PubSubConnectionHandler.register(
            new FriendRequestEventHandler(),
            new FriendResponseEventHandler()
    );
  }
}
