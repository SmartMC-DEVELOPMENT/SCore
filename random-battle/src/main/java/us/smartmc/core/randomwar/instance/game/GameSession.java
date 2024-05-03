package us.smartmc.core.randomwar.instance.game;

import lombok.Getter;

import java.util.UUID;

public class GameSession {

    @Getter
    private UUID id;

    private GameSession(UUID id) {
        this.id = id;
    }

}
