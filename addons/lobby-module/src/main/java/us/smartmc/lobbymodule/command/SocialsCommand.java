package us.smartmc.lobbymodule.command;

import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.smartmc.lobbymodule.menu.LinkSocialsMenu;
import us.smartmc.lobbymodule.util.PlayerUtil;
import us.smartmc.smartaddons.plugin.AddonPluginCommand;

import java.util.List;

public class SocialsCommand extends AddonPluginCommand {

    public SocialsCommand(String name) {
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
        try {
            new LinkSocialsMenu(player, args[0]).open(player);
        } catch (Exception e) {
            player.sendMessage(ChatUtil.parse(player, "<lang.lobby.link_socials_target_not_found>"));
        }
    }

    @Override
    public void executeAdminPlayer(Player player, String[] args) {}

    @Override
    public List<String> getAliases() {
        return List.of("redes", "mostrarRedes", "showSocials", "verRedes",
                "getRedes", "getSocials");
    }
}
