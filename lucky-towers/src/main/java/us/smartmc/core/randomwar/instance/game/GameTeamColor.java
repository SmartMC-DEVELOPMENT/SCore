package us.smartmc.core.randomwar.instance.game;

import lombok.Getter;
import net.kyori.adventure.text.format.NamedTextColor;

@Getter
public enum GameTeamColor {

    RED(NamedTextColor.RED),
    BLUE(NamedTextColor.BLUE),
    AQUA(NamedTextColor.AQUA),
    LIGHT_PURPLE(NamedTextColor.LIGHT_PURPLE),
    YELLOW(NamedTextColor.YELLOW),
    GOLD(NamedTextColor.GOLD),
    GREEN(NamedTextColor.GREEN),
    GRAY(NamedTextColor.GRAY),
    DARK_GRAY(NamedTextColor.DARK_GRAY),
    DARK_PURPLE(NamedTextColor.DARK_PURPLE),
    DARK_RED(NamedTextColor.DARK_RED),
    DARK_BLUE(NamedTextColor.DARK_BLUE);

    final NamedTextColor namedTextColor;

    GameTeamColor(NamedTextColor namedTextColor) {
        this.namedTextColor = namedTextColor;
    }


}
