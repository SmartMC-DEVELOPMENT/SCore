package us.smartmc.lobbymodule.command;

import me.imsergioh.pluginsapi.util.PaperChatUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.smartmc.lobbymodule.LobbyModule;
import us.smartmc.smartaddons.plugin.AddonPluginCommand;

public class LobbyCommand extends AddonPluginCommand {

    public LobbyCommand() {
        super("lobby", false);
    }

    @Override
    public void executeAll(CommandSender sender, String[] args) {
        sender.sendMessage(PaperChatUtil.parse(
                "&6lobby-module&a created by &bImSergioh &alet's go! :D (v" +
                        LobbyModule.getPlugin().getInfo().version() + ")"
        ));
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(Player player, String[] args) {
    }

    @Override
    public void executeAdminPlayer(Player player, String[] args) {
    }
}
