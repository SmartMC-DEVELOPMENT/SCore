package us.smartmc.gamescore.manager;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import us.smartmc.gamescore.api.GamesCoreAPI;
import us.smartmc.gamescore.instance.cmd.GamesCoreCommand;
import us.smartmc.gamescore.instance.manager.MapManager;

public class CoreCommandsManager extends MapManager<String, GamesCoreCommand> implements Listener {

    private CoreCommandsManager() {
        Bukkit.getPluginManager().registerEvents(this, GamesCoreAPI.getApi().getPlugin());
    }

    @Override
    public GamesCoreCommand createValueByKey(String key) {
        return null;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().startsWith("/")) {
            String label = event.getMessage().substring(1);
            perform(event.getPlayer(), label, event);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCommand(ServerCommandEvent event) {
        if (event.getSender() instanceof Player) return;
        String label = event.getCommand();
        perform(event.getSender(), label, event);
    }

    private void perform(CommandSender sender, String label, Cancellable event) {
        String name = label.split(" ")[0];
        if (!containsKey(name)) return;
        String[] args = label.replaceFirst(name + " ", "").split(" ");
        if (label.startsWith(" ")) label = label.substring(1);
        get(name).execute(sender, label, args);
        event.setCancelled(true);
    }

    @Override
    public GamesCoreCommand put(String name, GamesCoreCommand command) {
        SimpleCommandMap commandMap = getCommandMap();
        commandMap.register(name, command);
        return super.put(name, command);
    }

    private SimpleCommandMap getCommandMap() {
        return (SimpleCommandMap) Bukkit.getServer().getCommandMap();
    }

}
