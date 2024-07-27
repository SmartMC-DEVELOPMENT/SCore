package us.smartmc.game.luckytowers.variable;

import me.imsergioh.pluginsapi.instance.VariableListener;
import org.bukkit.entity.Player;
import us.smartmc.core.util.VariableUtil;
import us.smartmc.game.luckytowers.instance.player.GamePlayer;
import us.smartmc.game.luckytowers.instance.player.GamePlayerData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerStatsVariables extends VariableListener<Player> {

    @Override
    public String parse(String s) {
        return s;
    }

    @Override
    public String parse(Player player, String message) {
        message = replaceBigNumberStat(player, message, "<ltcoins>", GamePlayer.COINS_KEY);
        message = replaceBigNumberStat(player, message, "<wins>", GamePlayer.WINS_KEY);
        message = replaceBigNumberStat(player, message, "<kills>", GamePlayer.KILLS_KEY);
        message = replaceBigNumberStat(player, message, "<deaths>", GamePlayer.DEATHS_KEY);
        message = replaceBigNumberStat(player, message, "<gamesPlayed>", GamePlayer.GAMES_PLAYED_KEY);

        // Streaks (Best & current in one)
        if (message.contains("<streak") || message.contains("<bestStreak")) {
            boolean best = message.contains("<bestStreak");
            GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
            if (gamePlayer == null) return "NaN";
            String[] words = message.split(" ");

            String regex = "<(bestStreak|streak)([A-Za-z]+)>";
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

            for (String word : words) {
                Matcher matcher = pattern.matcher(word);
                if (matcher.find()) {
                    String streakStatIdName = matcher.group(2).toLowerCase();
                    if (best) streakStatIdName = GamePlayerData.getBestStreakKey(streakStatIdName);
                    GamePlayerData data = gamePlayer.getData();
                    long stat = best ? data.getBestStreak(streakStatIdName) : data.getStreak(streakStatIdName);
                    message = message.replace(word, String.valueOf(stat));
                }
            }
        }
        return message;
    }

    public String replaceBigNumberStat(Player player, String message, String token, String databaseKey) {
        return VariableUtil.replace(message, token, s -> {
            GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
            return String.valueOf(gamePlayer.getData().getBigNumber(databaseKey));
        });
    }
}
