package us.smartmc.moderation.staffmodebungee.command;

import me.imsergioh.pluginsapi.util.ChatUtil;
import me.imsergioh.smartcorewaterfall.instance.PlayerLanguages;
import me.imsergioh.smartcorewaterfall.messages.ProxyMainMessages;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import us.smartmc.moderation.staffmodebungee.StaffModeMainBungee;
import us.smartmc.moderation.staffmodebungee.manager.StaffChatManager;
import us.smartmc.moderation.staffmodebungee.util.RegistrationUtil;

public class StaffChatCommand extends Command {

    private static final StaffChatManager chatManager = StaffModeMainBungee.getChatManager();

    public StaffChatCommand() {
        super("staffChat");
    }

    @Override
    public String[] getAliases() {
        return new String[]{"sc", "staffchat", "schat", "staffc"};
    }

    public void executeStaffPlayer(ProxiedPlayer player, String[] args) {
        if (args.length == 0) {
            chatManager.toggleChat(player);
            return;
        }

        if (args[0].equals("toggle")) {
            chatManager.toggleVisibility(player);
            return;
        }
    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(ChatColor.RED + "Utilidad de este comando para consola aún no implementado.");
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (!sender.hasPermission(RegistrationUtil.STAFF_PERMISSION)) {
            player.sendMessage(ChatUtil.parse(player, ProxyMainMessages.get(PlayerLanguages.getLanguage(player.getUniqueId()),
                    "no_permission")));
            return;
        }
        executeStaffPlayer(player, args);
    }
}
