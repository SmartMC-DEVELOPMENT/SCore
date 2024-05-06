package us.smartmc.game.luckytowers.instance.game;

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

    public static GameTeamColor rotate(int indice) {
        GameTeamColor[] valores = GameTeamColor.values();
        int cantidadValores = valores.length;
        int indiceRotado = indice % cantidadValores;
        if (indiceRotado < 0) {
            indiceRotado += cantidadValores;
        }
        return valores[indiceRotado];
    }

    public GameTeamColor next() {
        int indiceActual = this.ordinal();
        return rotate(indiceActual + 1);
    }


}
