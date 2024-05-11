package us.smartmc.game.luckytowers.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import us.smartmc.core.handler.SpawnHandler;
import us.smartmc.game.luckytowers.LuckyTowers;
import us.smartmc.game.luckytowers.instance.game.GameSession;
import us.smartmc.game.luckytowers.instance.player.GamePlayer;

public class LeaveCommand implements CommandExecutor {

    private static final LuckyTowers plugin = LuckyTowers.getPlugin();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        return false;
    }

    public static void leave(Player player) {
        GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
        GameSession session = gamePlayer.getGameSession();

        if (session == null) return;
        session.quitPlayer(gamePlayer);

        // Teleport to spawn if is online
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            gamePlayer.onlinePlayer(p -> {
                p.teleport(SpawnHandler.getLocation());
            });
        }, 1);
    }

}
