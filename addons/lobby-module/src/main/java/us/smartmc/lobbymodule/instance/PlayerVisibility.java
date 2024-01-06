package us.smartmc.lobbymodule.instance;

import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import me.imsergioh.pluginsapi.language.Language;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public enum PlayerVisibility {

    DEFAULT, VIPS, NO_ONE;

    public static List<String> getOptionItemLore(Player player) {
        CorePlayer corePlayer = CorePlayer.get(player);
        Language language = PlayerLanguages.get(player.getUniqueId());
        String visibilityName = corePlayer.getPlayerData().getDocument().get("visibility", String.class);

        if (visibilityName == null) visibilityName = DEFAULT.toString();

        PlayerVisibility selected = PlayerVisibility.valueOf(visibilityName);
        StringBuilder builder = new StringBuilder();
        for (PlayerVisibility visibility : values()) {
            boolean optionSelected = visibility == selected;
            builder.append(optionLine(language, visibility, optionSelected));
        }
        return Arrays.asList(builder.toString().split("\n"));
    }

    private static String optionLine(Language language, PlayerVisibility visibility, boolean selected) {
        StringBuilder builder = new StringBuilder();
        if (selected) {
            builder.append("&f&l• ");
        } else {
            builder.append("&7• ");
        }
        builder.append(nameOf(language, visibility));
        builder.append("\n");
        return builder.toString();
    }

    private static String nameOf(Language language, PlayerVisibility visibility) {
        return LanguagesHandler.get(language).get("lobby")
                .getString("visibility_" + visibility.name() + "_name");
    }

}
