package me.imsergioh.smartcorewaterfall.listener;

import me.imsergioh.smartcorewaterfall.manager.friend.FriendsManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class FriendAndPartyListeners implements Listener {

  @EventHandler(priority = 69)
  public void send(ServerConnectedEvent event) {
    final ProxiedPlayer player = event.getPlayer();
    FriendsManager.joinFriendStatusCheck(player);
  }
}
