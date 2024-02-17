package us.smartmc.lobbymodule.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.smartmc.core.instance.player.SmartCorePlayer;
import us.smartmc.lobbymodule.handler.FlyManager;
import us.smartmc.lobbymodule.menu.LinkSocialsMenu;
import us.smartmc.lobbymodule.util.PlayerUtil;
import us.smartmc.smartaddons.plugin.AddonPluginCommand;

public class RedesCommand extends AddonPluginCommand {

    public RedesCommand(String name) {
        super(name);
    }

    @Override
    public void executeAll(CommandSender sender, String[] args) {
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
        sender.sendMessage("Only for players.");
    }

    @Override
    public void executePlayer(Player player, String[] args) {
        if (args.length == 0) {
            PlayerUtil.send(player, "lobby", "link_socials.socials_cmd.usage");
            return;
        }

        new LinkSocialsMenu(player).open(player);
    }

    @Override
    public void executeAdminPlayer(Player player, String[] args) {}
}
