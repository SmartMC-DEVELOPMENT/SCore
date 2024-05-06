package us.smartmc.game.luckytowers.command;

import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.util.PaperChatUtil;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import us.smartmc.game.luckytowers.LuckyTowers;
import us.smartmc.game.luckytowers.config.MainPluginConfig;
import us.smartmc.game.luckytowers.instance.game.GameMap;
import us.smartmc.game.luckytowers.manager.EditorModeManager;
import us.smartmc.game.luckytowers.manager.GameMapManager;
import us.smartmc.game.luckytowers.messages.GameMessages;

public class AdminGameCommand implements CommandExecutor {

    private static final LuckyTowers plugin = LuckyTowers.getPlugin();

    private void executePlayer(Player player, String label, String[] args) {
        if (args.length >= 1 && args[0].equalsIgnoreCase("setLobby")) {
            plugin.getMainConfig().setLobby(player.getLocation());
            PaperChatUtil.send(player, GameMessages.cmd_adminGame_lobbySet);
            return;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("lobby")) {
            Location lobbyLocation = MainPluginConfig.getLobby();
            player.teleport(lobbyLocation);
            return;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("editor")) {
            EditorModeManager manager = LuckyTowers.getManager(EditorModeManager.class);
            manager.toggle(player);
        }

        if (args.length >= 2 && args[0].equalsIgnoreCase("createMap")) {
            String name = args[1];
            GameMapManager manager = LuckyTowers.getManager(GameMapManager.class);
            manager.register(name, new GameMap(name));
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("*")) {
            Language language = sender instanceof Player player ? PlayerLanguages.get(player.getUniqueId()) : Language.getDefault();
            String message = GameMessages.cmd_adminGame_noPermission.getMessageOf(language);
            sender.sendMessage(message);
        }

        if (sender instanceof Player player) {
            executePlayer(player, label, args);
        }
        return false;
    }
}
