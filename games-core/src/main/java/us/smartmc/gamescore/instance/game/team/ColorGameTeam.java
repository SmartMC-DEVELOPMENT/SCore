package us.smartmc.gamescore.instance.game.team;

import lombok.Getter;

@Getter
public class ColorGameTeam extends GameTeam {

    private final ColorGameTeamColor color;

    public ColorGameTeam(ColorGameTeamColor color) {
        super(color.getName());
        this.color = color;
    }
}
