
package us.smartmc.smartcore.proxy.listener;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.event.TabCompleteResponseEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import us.smartmc.smartcore.proxy.manager.AllowedCommandsManager;

import java.util.*;

public class AllowedCommandsListeners implements Listener {

    private static final Map<UUID, String> tabCompletations = new HashMap<>();
    private static final Map<UUID, Timer> timers = new HashMap<>();

    @EventHandler(priority = 100)
    public void onTabComplete(TabCompleteEvent event) {
        if (!(event.getSender() instanceof ProxiedPlayer player)) return;


        String label = event.getCursor();

        tabCompletations.put(player.getUniqueId(), label);

        if (timers.containsKey(player.getUniqueId())) {
            timers.get(player.getUniqueId()).cancel();
            timers.get(player.getUniqueId()).purge();
        }

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                tabCompletations.remove(player.getUniqueId());
            }
        }, 100);

        timers.put(player.getUniqueId(), timer);

        if (!label.startsWith("/")) {
            event.setCancelled(false);
            return;
        }

        String name = label.split(" ")[0];

        if (name.startsWith("/ver") || name.contains(":")) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "Stop that shit.");
        }

        if (label.endsWith(" ")) {
            event.setCancelled(false);
            return;
        }

        if (label.split(" ").length != 1) return;
        String serverName = player.getServer().getInfo().getName();

        event.getSuggestions().clear();

        for (AllowedCommandsManager manager : AllowedCommandsManager.get(serverName)) {
            List<String> commands = manager.getAllowedCommands();
            commands.forEach(s -> {
                if (!s.startsWith("/")) s = "/" + s;
                if (!s.startsWith(label)) return;
                event.getSuggestions().add(s);
            });
        }
        event.setCancelled(false);
    }

    @EventHandler(priority = 90)
    public void onTab(TabCompleteResponseEvent event) {
        if (!(event.getReceiver() instanceof ProxiedPlayer player)) return;
        event.setCancelled(true);

    }
}
