package us.smartmc.game.luckytowers.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import us.smartmc.game.luckytowers.LuckyTowers;
import us.smartmc.game.luckytowers.instance.player.EditorSession;
import us.smartmc.game.luckytowers.manager.EditorModeManager;

import java.lang.reflect.Field;

public class EditorListeners implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent event) {
        EditorSession session = get(event.getPlayer());
        String message = event.getMessage();
        if (session == null) return;
        if (!message.startsWith("@") && !message.contains(" ")) return;

        String fieldName = message.split(" ")[0].replace("@", "");
        String value = message.replace("@" + fieldName + " ", "");
        try {
            Field field = session.getClass().getDeclaredField(fieldName);
            boolean wasAccesible = field.canAccess(session);
            field.setAccessible(true);
            field.set(session, value);
            field.setAccessible(wasAccesible);
        } catch (Exception e) {
            event.getPlayer().sendMessage("Error al intentar establecer field!");
            throw new RuntimeException(e);
        }
        event.setCancelled(true);
    }

    private static EditorSession get(Player player) {
        EditorModeManager manager = LuckyTowers.getManager(EditorModeManager.class);
        return manager.get(player.getUniqueId());
    }

}
