package us.smartmc.core.randomwar.command;

import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.util.PaperChatUtil;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import us.smartmc.core.randomwar.RandomBattle;
import us.smartmc.core.randomwar.config.MainPluginConfig;
import us.smartmc.core.randomwar.manager.EditorModeManager;
import us.smartmc.core.randomwar.messages.GameMessages;

public class AdminGameCommand implements CommandExecutor {

    private static final RandomBattle plugin = RandomBattle.getPlugin();

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
            EditorModeManager.toggle(player);
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
