package me.imsergioh.smartcorewaterfall.manager.cooldown.implementation.friend;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.util.ChatUtil;
import me.imsergioh.smartcorewaterfall.SmartCoreWaterfall;
import me.imsergioh.smartcorewaterfall.instance.OfflinePlayerData;
import me.imsergioh.smartcorewaterfall.instance.PlayerLanguages;
import me.imsergioh.smartcorewaterfall.messages.FriendManagerMessages;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class FriendRequestObject implements IFriendRequest {

  private final UUID senderUUID;
  private final UUID receiverUUID;

  private FriendCooldownStatus status = FriendCooldownStatus.PENDING;

  public static IFriendRequest fromString(String data) {
    final String[] dataInput = data.split("\\.");
    if (dataInput.length != 3) {
      return null;
    }

    final FriendRequestObject object = new FriendRequestObject(
            UUID.fromString(dataInput[1]),
            UUID.fromString(dataInput[2])
    );
    object.setStatus(FriendCooldownStatus.valueOf(dataInput[0]));
    
    return object;
  }

  public OfflinePlayerData getSenderOfflineData() {
    return OfflinePlayerData.get(this.getSenderUUID());
  }

  public OfflinePlayerData getReceiverOfflineData() {
    return OfflinePlayerData.get(this.getReceiverUUID());
  }

  /**
   * @return null if the sender isn't connected or the ProxiedPlayer
   */
  public ProxiedPlayer getSender() {
    final ProxyServer server = SmartCoreWaterfall.getPlugin().getProxy();
    if (server == null) {
      return null;
    }
    return server.getPlayer(this.getSenderUUID());
  }

  /**
   * @return null if the receiver isn't connected or the ProxiedPlayer
   */
  public ProxiedPlayer getReceiver() {
    final ProxyServer server = SmartCoreWaterfall.getPlugin().getProxy();
    if (server == null) {
      return null;
    }
    return server.getPlayer(this.getReceiverUUID());
  }

  public boolean requestFriendIfConnected() {
    final OfflinePlayerData senderPlayerData = this.getSenderOfflineData();
    final ProxiedPlayer receiver = this.getReceiver();

    if (senderPlayerData == null || receiver == null) {
      return false;
    }

    PlayerLanguages.sendMessage(receiver, FriendManagerMessages.NAME,
            "request_received",

            senderPlayerData.getName(),
            receiver.getName(),
            FriendCooldownImpl.REQUEST_EXPIRATION_MINUTES
    );
    return true;
  }

  @Override
  public boolean answerFriendRequest() {
    final ProxiedPlayer sender = this.getSender();
    final OfflinePlayerData receiverPlayerData = this.getReceiverOfflineData();

    if (sender == null || receiverPlayerData == null) {
      return false;
    }

    final String statusValue = "response_status_" + this.getStatus().name().toLowerCase();
    final String translatableValue = ChatUtil.parse(LanguagesHandler
            .get(PlayerLanguages.getLanguage(sender))
            .get(FriendManagerMessages.NAME)
            .getString(statusValue)
    );

    PlayerLanguages.sendMessage(sender, FriendManagerMessages.NAME,
            "response_received",
            receiverPlayerData.getName(),
            sender.getName(),
            translatableValue
    );
    return true;
  }
}
