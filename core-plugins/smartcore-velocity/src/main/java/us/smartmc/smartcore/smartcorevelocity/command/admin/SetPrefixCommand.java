package us.smartmc.smartcore.smartcorevelocity.command.admin;

import com.velocitypowered.api.command.CommandSource;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import us.smartmc.smartcore.smartcorevelocity.instance.CoreCommand;
import me.imsergioh.pluginsapi.manager.VelocityPluginsAPI;

public class SetPrefixCommand extends CoreCommand {

    public SetPrefixCommand() {
        super("setPrefix");
    }

    @Override
    public void execute(CommandSource sender, String[] args) {
        if (!sender.hasPermission("*")) {
            sender.sendMessage(Component.text(NamedTextColor.RED + "Not permitted to execute this command."));
            return;
        }
        // USAGE: /setPrefix <rank> <color>
        if (args.length == 2) {
            String prefix = ("[" + args[0] + "]").toUpperCase();
            String command = "lpb group " + args[0] + " meta setprefix \"&" + args[1].replace("&", "") + prefix + "\"";
            CommandSource proxySender = VelocityPluginsAPI.proxy.getConsoleCommandSource();
            VelocityPluginsAPI.proxy.getCommandManager().executeAsync(proxySender, command);
        } else {
            sender.sendMessage(Component.text(NamedTextColor.RED + "Correct usage: /setPrefix <rankName> <color>"));
        }
    }
}
