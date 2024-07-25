package us.smartmc.game.luckytowers.instance.game;

import lombok.Getter;
import org.bukkit.ChatColor;

@Getter
public enum GameTeamColor {

    RED(ChatColor.RED),
    BLUE(ChatColor.BLUE),
    AQUA(ChatColor.AQUA),
    LIGHT_PURPLE(ChatColor.LIGHT_PURPLE),
    YELLOW(ChatColor.YELLOW),
    GOLD(ChatColor.GOLD),
    GREEN(ChatColor.GREEN),
    GRAY(ChatColor.GRAY),
    DARK_GRAY(ChatColor.DARK_GRAY),
    DARK_PURPLE(ChatColor.DARK_PURPLE),
    DARK_RED(ChatColor.DARK_RED),
    DARK_BLUE(ChatColor.DARK_BLUE);

    final ChatColor namedTextColor;

    GameTeamColor(ChatColor namedTextColor) {
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
