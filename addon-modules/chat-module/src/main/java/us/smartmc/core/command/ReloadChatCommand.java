package us.smartmc.core.command;

import me.imsergioh.pluginsapi.util.PaperChatUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.smartmc.core.ChatModule;
import us.smartmc.smartaddons.plugin.AddonPluginCommand;

public class ReloadChatCommand extends AddonPluginCommand {

    public ReloadChatCommand(String name, boolean forAdmins, String permissions) {
        super(name, forAdmins, permissions);
    }

    @Override
    public void executeAll(CommandSender commandSender, String[] strings) {
        String permission = getPermission();
        if (permission == null) return;
        if (!commandSender.hasPermission(getPermission())) return;
        ChatModule.getModule().reload();
        commandSender.sendMessage(PaperChatUtil.parse("&aConfiguración de chat recargada correctamente!"));
    }

    @Override
    public void executeConsole(CommandSender commandSender, String[] strings) {

    }

    @Override
    public void executePlayer(Player player, String[] strings) {

    }

    @Override
    public void executeAdminPlayer(Player player, String[] strings) {

    }
}
