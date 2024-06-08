package us.smartmc.game.luckytowers.command;

import me.imsergioh.pluginsapi.util.PaperChatUtil;
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
import us.smartmc.game.luckytowers.messages.GameMessages;

public class ForceStartCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;
        if (!player.hasPermission("smartmc.vip")) return true;
        GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
        if (gamePlayer == null) return true;
        GameSession session = gamePlayer.getGameSession();
        if (session == null) return true;
        session.forceStart();
        PaperChatUtil.send(player, GameMessages.forceStarting);
        return false;
    }
}
