package us.smartmc.core.randomwar.manager;

import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import me.imsergioh.pluginsapi.util.PaperChatUtil;
import org.bukkit.entity.Player;
import us.smartmc.core.randomwar.menu.EditorModeHotbar;
import us.smartmc.core.randomwar.messages.GameMessages;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class EditorModeManager {

    private static final Set<UUID> editors = new HashSet<>();

    public static boolean toggle(Player player) {
        if (hasEditorMode(player)) {
            return disable(player);
        }
        return enable(player);
    }

    public static boolean enable(Player player) {
        PaperChatUtil.send(player, GameMessages.editorMode_enabled);
        new EditorModeHotbar(player).set(player);
        editors.add(player.getUniqueId());
        return true;
    }

    public static boolean disable(Player player) {
        PaperChatUtil.send(player, GameMessages.editorMode_disabled);
        CorePlayer corePlayer = CorePlayer.get(player);

        if (corePlayer.getCurrentMenuSet() instanceof EditorModeHotbar editorModeHotbar) {
            editorModeHotbar.restore(player);
        }
        editors.remove(player.getUniqueId());
        return false;
    }

    public static boolean hasEditorMode(Player player) {
        return editors.contains(player.getUniqueId());
    }
}
