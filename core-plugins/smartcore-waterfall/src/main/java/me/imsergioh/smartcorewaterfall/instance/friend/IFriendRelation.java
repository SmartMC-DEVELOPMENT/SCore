package me.imsergioh.smartcorewaterfall.instance.friend;

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
    return new Document("_id", this.getPlayer())
            .append("timestamp", this.getFriendTimestamp())
            .append("bestFriend", this.isBestFriend());
  }

  static IFriendRelation fromDocument(Document document) {
    final UUID playerId = document.get("_id", UUID.class);

    final long timestamp = document.getLong("timestamp");
    final boolean bestFriend = document.getBoolean("bestFriend", false);

    return new IFriendRelation() {
      @Override
      public UUID getPlayer() {
        return playerId;
      }

      @Override
      public boolean isBestFriend() {
        return bestFriend;
      }

      @Override
      public long getFriendTimestamp() {
        return timestamp;
      }
    };
  }
}
