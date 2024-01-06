package us.smartmc.core.instance.player.friend;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class FriendRelation implements IFriendRelation {

  private final UUID player;
  private final long friendTimestamp;

  @Setter
  private boolean bestFriend;
}
