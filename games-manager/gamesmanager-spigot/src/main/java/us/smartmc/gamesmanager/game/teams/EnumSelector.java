package us.smartmc.gamesmanager.game.teams;

import java.lang.reflect.Array;
import java.util.EnumSet;

public class EnumSelector<T extends Enum<T>> {

    private int teamSelectedIndex = 0;

    public T alternate() {
        T[] values = (T[]) EnumSet.allOf(PlayerTeam.class).toArray((T[]) Array.newInstance(PlayerTeam.class, 0));
        int teamsAmount = values.length;
        teamSelectedIndex++;
        if (teamSelectedIndex == teamsAmount) {
            teamSelectedIndex = 0;
        }
        return getSelected();
    }

    public T getSelected() {
        T[] values = (T[]) EnumSet.allOf(PlayerTeam.class).toArray((T[]) Array.newInstance(PlayerTeam.class, 0));
        return values[teamSelectedIndex];
    }

}
