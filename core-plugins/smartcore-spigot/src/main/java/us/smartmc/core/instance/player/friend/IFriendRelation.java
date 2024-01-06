package us.smartmc.core.instance.player.friend;

import org.bson.Document;

import java.time.Duration;
import java.util.UUID;

public interface IFriendRelation {

  UUID getPlayer();

  boolean isBestFriend();

  void setBestFriend(boolean bestFriend);

  long getFriendTimestamp();

  default Duration getFriendDuration() {
    return Duration.ofMillis(System.currentTimeMillis() - getFriendTimestamp());
  }

  default Document toDocument() {
    return new Document("_id", this.getPlayer().toString())
            .append("timestamp", this.getFriendTimestamp())
            .append("bestFriend", this.isBestFriend());
  }

  static IFriendRelation fromDocument(Document document) {
    final UUID playerId = UUID.fromString(document.getString("_id"));

    final long timestamp = document.getLong("timestamp");
    final boolean bestFriend = document.getBoolean("bestFriend", false);

    final FriendRelation friendRelation = new FriendRelation(playerId, timestamp);
    friendRelation.setBestFriend(bestFriend);
    return friendRelation;
  }
}
