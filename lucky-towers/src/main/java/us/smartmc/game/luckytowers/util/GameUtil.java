package us.smartmc.game.luckytowers.util;

import org.bukkit.Material;
import us.smartmc.core.SmartCore;
import us.smartmc.core.handler.ScoreboardHandler;
import us.smartmc.game.luckytowers.instance.game.GameSession;
import us.smartmc.game.luckytowers.instance.game.GameSessionStatus;
import us.smartmc.game.luckytowers.instance.player.GamePlayer;
import us.smartmc.game.luckytowers.instance.player.PlayerScoreboardType;

import java.util.Random;

public class GameUtil {

    public static Material getRandomMaterial() {
        int lenght = Material.values().length - 1;
        int randomIndex = new Random().nextInt(lenght);
        return Material.values()[randomIndex];
    }

    public static void updateScoreboard(GamePlayer gamePlayer) {
        ScoreboardHandler handler = SmartCore.getPlugin().getScoreboardHandler();
        handler.register(gamePlayer.getBukkitPlayer(), getScoreboardTypeByPlayer(gamePlayer).getId());
    }

    public static PlayerScoreboardType getScoreboardTypeByPlayer(GamePlayer gamePlayer) {
        GameSession session = gamePlayer.getGameSession();
        if (session == null) return PlayerScoreboardType.DEFAULT;
        switch (gamePlayer.getStatus()) {
            case INGAME -> {
                if (session.getStatus().equals(GameSessionStatus.STARTING)) {
                    return PlayerScoreboardType.STARTING;
                }
                if (session.getStatus().equals(GameSessionStatus.WAITING)) {
                    return PlayerScoreboardType.WAITING;
                }

                return PlayerScoreboardType.PLAYING;
            }
            case SPECTATING -> {
                return PlayerScoreboardType.SPECTATING;
            }
            default -> {
                return PlayerScoreboardType.DEFAULT;
            }
        }
    }

}
