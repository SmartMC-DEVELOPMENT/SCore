package us.smartmc.core.listener;

import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import us.smartmc.core.instance.CommandIntermediate;


public class CommandListeners implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPreProcessCommand(PlayerCommandPreprocessEvent event) {
        String name = event.getMessage().contains(" ") ?
                event.getMessage().split(" ")[0] : event.getMessage();

        if (event.isCancelled()) return;
        CommandIntermediate intermediate = new CommandIntermediate(name);

        if (!intermediate.isAtCommandMap()) {
            event.getPlayer().sendMessage(ChatUtil.parse(event.getPlayer(), "<lang.lobby.unknown_command>"));
            event.setCancelled(true);
        }
    }

}

