package us.smartmc.moderation.staffmodebungee;

import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import us.smartmc.moderation.staffmodebungee.command.StaffChatCommand;
import us.smartmc.moderation.staffmodebungee.listener.ManagersEvents;
import us.smartmc.moderation.staffmodebungee.manager.MessagesManager;
import us.smartmc.moderation.staffmodebungee.manager.StaffChatManager;
import us.smartmc.moderation.staffmodebungee.manager.StaffPlayerManager;
import us.smartmc.moderation.staffmodebungee.util.RegistrationUtil;

public final class StaffModeMainBungee extends Plugin {

    @Getter
    private static StaffModeMainBungee plugin;

    @Getter
    public static StaffPlayerManager playerManager;

    @Getter
    private static StaffChatManager chatManager;

    @Override
    public void onEnable() {
        plugin = this;
        MessagesManager.load();
        playerManager = new StaffPlayerManager();
        chatManager = new StaffChatManager();
        RegistrationUtil.registerCommands(plugin, new StaffChatCommand());
        RegistrationUtil.registerListeners(plugin, new ManagersEvents());
    }

    @Override
    public void onDisable() {

    }
}
