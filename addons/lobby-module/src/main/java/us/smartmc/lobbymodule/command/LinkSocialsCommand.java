package us.smartmc.lobbymodule.command;

import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.smartmc.lobbymodule.menu.LinkSocialsMenu;
import us.smartmc.smartaddons.plugin.AddonPluginCommand;

public class LinkSocialsCommand extends AddonPluginCommand {

    public LinkSocialsCommand(String name) {
        super(name, false, "*");
    }

    @Override
    public void executeAll(CommandSender commandSender, String[] args) {}

    @Override
    public void executeConsole(CommandSender commandSender, String[] args) {}

    @Override
    public void executePlayer(Player player, String[] args) {
        player.closeInventory();
        if (player.hasPermission(getPermission())) {
            executeAdminPlayer(player, args);
            return;
        }
        player.sendMessage(ChatUtil.parse(player, "<lang.lobby.feature_in_development>"));
    }

    @Override
    public void executeAdminPlayer(Player player, String[] args) {
        player.sendMessage("Executing admin action! This is only available in admin mode!!");
        new LinkSocialsMenu(player).open(player);
    }
}
