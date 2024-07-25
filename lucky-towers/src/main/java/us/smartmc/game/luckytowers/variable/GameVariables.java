package us.smartmc.game.luckytowers.variable;

import me.imsergioh.pluginsapi.instance.VariableListener;
import org.bukkit.entity.Player;
import us.smartmc.core.util.VariableUtil;
import us.smartmc.game.luckytowers.instance.game.GameSession;
import us.smartmc.game.luckytowers.instance.player.GamePlayer;
import us.smartmc.game.luckytowers.util.GameUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameVariables extends VariableListener<Player> {

    @Override
    public String parse(String s) {
        return s;
    }

    @Override
    public String parse(Player player, String message) {
        message = VariableUtil.replace(message, "<countdown>", s -> GameUtil.getFormattedTimeFromSeconds(GamePlayer.get(player.getUniqueId()).getGameSession().getCountdown()));
        message = VariableUtil.replace(message, "<timeRemaining>", s -> GameUtil.getFormattedTimeFromSeconds(GamePlayer.get(player.getUniqueId()).getGameSession().getSecondsRemaining()));

        message = VariableUtil.replace(message, "<playersRemaining>", s -> {
            GameSession session = GamePlayer.get(player.getUniqueId()).getGameSession();
            return session.getAlivePlayers().size() + "/" + session.getMap().getSpawnLocations().size();
        });

        message = VariableUtil.replace(message, "<playersRemainingShort>", s -> {
            GameSession session = GamePlayer.get(player.getUniqueId()).getGameSession();
            return session.getAlivePlayers().size() + "";
        });

        message = VariableUtil.replace(message, "<spectatorsCount>", s -> {
            GameSession session = GamePlayer.get(player.getUniqueId()).getGameSession();
            return String.valueOf(session.getSpectatorsCount());
        });

        message = VariableUtil.replace(message, "<mapName>", s -> {
            GameSession session = GamePlayer.get(player.getUniqueId()).getGameSession();
            return session.getMap().getName();
        });


        if (message.contains("<mapPlaying.")) {
            // Patrón para buscar contenido entre <mapPlaying. y >
            Pattern pattern = Pattern.compile("<mapPlaying\\.(.*?)>");
            Matcher matcher = pattern.matcher(message);

            // Iterar sobre todas las coincidencias encontradas
            while (matcher.find()) {
                // Obtener el contenido dentro de <mapPlaying. y >
                String mapPlayingContent = matcher.group(1);
                System.out.println(mapPlayingContent);
            }
        }

        return message;
    }
}
