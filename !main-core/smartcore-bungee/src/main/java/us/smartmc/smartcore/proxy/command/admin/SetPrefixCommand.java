package us.smartmc.smartcore.proxy.command.admin;

import me.imsergioh.pluginsapi.manager.BungeeCordPluginsAPI;
import me.imsergioh.pluginsapi.util.ChatUtil;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.CommandSender;
import us.smartmc.smartcore.proxy.instance.CoreCommand;

public class SetPrefixCommand extends CoreCommand {

    public SetPrefixCommand() {
        super("setPrefix");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("*")) {
            sender.sendMessage(ChatUtil.parse(NamedTextColor.RED + "Not permitted to execute this command."));
            return;
        }
        // USAGE: /setPrefix <rank> <color>
        if (args.length == 2) {
            String prefix = ("[" + args[0] + "]").toUpperCase();
            String command = "lpb group " + args[0] + " meta setprefix \"&" + args[1].replace("&", "") + prefix + "\"";
            CommandSender proxySender = BungeeCordPluginsAPI.proxy.getConsole();
            BungeeCordPluginsAPI.proxy.getPluginManager().dispatchCommand(proxySender, command);
        } else {
            sender.sendMessage(ChatUtil.parse(NamedTextColor.RED + "Correct usage: /setPrefix <rankName> <color>"));
        }
    }
}
