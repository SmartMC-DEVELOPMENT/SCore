package us.smartmc.gamesmanager.game.teams;

import lombok.Getter;

public enum PlayerTeam {

    RED("c"),
    BLUE("9"),
    YELLOW("e"),
    GREEN("a"),
    VIOLET("d"),
    ORANGE("6");

    @Getter
    final String color;

    PlayerTeam(String color) {
        this.color = color;
    }
}
