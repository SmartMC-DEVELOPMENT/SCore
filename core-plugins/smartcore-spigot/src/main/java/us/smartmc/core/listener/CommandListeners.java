package us.smartmc.core.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import us.smartmc.core.instance.CommandIntermediate;
import us.smartmc.core.pluginsapi.util.ChatUtil;


public class CommandListeners implements Listener {

    @EventHandler
    public void onPreProcessCommand(PlayerCommandPreprocessEvent event) throws Exception {
        String name = event.getMessage().split(" ")[0];

        CommandIntermediate intermediate = new CommandIntermediate(name);

        if (!intermediate.isAtCommandMap()) {
            event.getPlayer().sendMessage(ChatUtil.parse(event.getPlayer(), "<lang.lobby.unknown_command>"));
            event.setCancelled(true);
        }
    }

}

