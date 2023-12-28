package me.imsergioh.smartcorewaterfall.instance.friend;

import com.google.common.base.Charsets;
import lombok.AccessLevel;
import lombok.Getter;
import me.imsergioh.pluginsapi.connection.RedisConnection;
import me.imsergioh.pluginsapi.instance.MongoDBPluginConfig;
import me.imsergioh.smartcorewaterfall.util.AsyncUtilities;
import org.bson.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
public class PlayerFriends extends MongoDBPluginConfig {

  protected static final String DATABASE = "proxy_data";
  protected static final String COLLECTION = "player_friends";

  private final UUID playerUuid;

  private final Map<UUID, IFriendRelation> friendList = new HashMap<>();

  public PlayerFriends(UUID playerUuid) {
    super(
            PlayerFriends.DATABASE,
            PlayerFriends.COLLECTION,
            new Document("_id", playerUuid)
    );
    this.playerUuid = playerUuid;
  }

  public void loadDocument() {
    this.load();

    final var friends = this.getQueryDocument().get("friends", List.class);
    for (Object friend : friends) {
      if (!(friend instanceof Document friendDocument)) {
        continue;
      }

      final IFriendRelation friendRelation = IFriendRelation.fromDocument(friendDocument);
      this.getFriendList().put(friendRelation.getPlayer(), friendRelation);
    }
  }

  public PlayerFriends(Document document) {
    super(PlayerFriends.DATABASE, PlayerFriends.COLLECTION, document);
    this.playerUuid = this.getQueryDocument().get("_id", UUID.class);

    this.loadDocument();
  }

  @Getter(AccessLevel.PRIVATE)
  private long lastUpdateFriends;

  public void saveFriends() {
    final long currentTimestamp = System.currentTimeMillis();
    this.lastUpdateFriends = currentTimestamp;

    AsyncUtilities.schedule(() -> {
      final var friendList = this.getFriendList()
              .values().stream().map(IFriendRelation::toDocument).toList();
      if (friendList.isEmpty()) return;

      if (currentTimestamp != this.getLastUpdateFriends()) {
        return;
      }

      this.put("friends", friendList);
      this.save();
    });
  }

  @Override
  public MongoDBPluginConfig save() {
    final var redisCache = "cache.friend.%s".formatted(this.getPlayerUuid()).getBytes(Charsets.UTF_8);

    final RedisConnection redisConnection = RedisConnection.mainConnection;
    if (redisConnection != null) {
      redisConnection.getResource().del(redisCache);
    }

    return super.save();
  }

  /* Can be nullable */
  public IFriendRelation getFriendRelation(UUID playerUuid) {
    return this.getFriendList().get(playerUuid);
  }

  public void addFriendRelation(IFriendRelation friendRelation) {
    this.getFriendList().put(friendRelation.getPlayer(), friendRelation);
    this.saveFriends();
  }

  public void addFriendRelation(UUID playerUuid, boolean isBest, long timestamp) {
    final IFriendRelation friendRelation = this.getFriendList().computeIfAbsent(playerUuid,
            uuid -> new FriendRelation(uuid, timestamp)
    );
    friendRelation.setBestFriend(isBest);
    this.saveFriends();
  }

  public void addFriendRelation(UUID playerUuid, boolean isBest) {
    this.addFriendRelation(playerUuid, isBest, System.currentTimeMillis());
  }

  public void addFriendRelation(UUID playerUuid, long timestamp) {
    this.getFriendList().computeIfAbsent(playerUuid, uuid -> new FriendRelation(uuid, timestamp));
    this.saveFriends();
  }

  public void removeFriendRelation(UUID playerUuid) {
    this.getFriendList().remove(playerUuid);
    this.saveFriends();
  }
}
