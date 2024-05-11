package us.smartmc.game.luckytowers.instance.player;

import lombok.Getter;

@Getter
public enum PlayerScoreboardType {

    DEFAULT("main"), WAITING("game_waiting"), STARTING("game_starting"), PLAYING("game_playing"), SPECTATING("game_spectating");

    private final String id;

    PlayerScoreboardType(String id) {
        this.id = id;
    }

}
