package us.smartmc.gamesmanager.gamesmanagerspigot.manager;

import us.smartmc.gamesmanager.gamesmanagerspigot.instance.player.GamePlayer;

import java.util.UUID;

public interface IGamePlayerManager<PlayerType extends GamePlayer> {

    <T extends GamePlayer> T createGamePlayer(UUID uuid);

}
