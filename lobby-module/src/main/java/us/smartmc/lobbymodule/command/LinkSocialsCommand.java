package us.smartmc.lobbymodule.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.smartmc.lobbymodule.menu.LinkSocialsMenu;
import us.smartmc.smartaddons.plugin.AddonPluginCommand;

public class LinkSocialsCommand extends AddonPluginCommand {

    public LinkSocialsCommand(String name) {
        super(name);
    }

    @Override
    public void executeAll(CommandSender commandSender, String[] args) {}

    @Override
    public void executeConsole(CommandSender commandSender, String[] args) {}

    @Override
    public void executePlayer(Player player, String[] args) {
        new LinkSocialsMenu(player).open(player);
    }

    @Override
    public void executeAdminPlayer(Player player, String[] strings) {

    }
}
