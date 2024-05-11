package us.smartmc.game.luckytowers.manager;

import me.imsergioh.pluginsapi.instance.manager.ManagerRegistry;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import me.imsergioh.pluginsapi.util.PaperChatUtil;
import org.bukkit.entity.Player;
import us.smartmc.game.luckytowers.instance.player.EditorSession;
import us.smartmc.game.luckytowers.menu.hotbar.EditorModeHotbar;
import us.smartmc.game.luckytowers.messages.GameMessages;

import java.util.*;

public class EditorModeManager extends ManagerRegistry<UUID, EditorSession> {

    public void toggle(Player player) {
        if (hasEditorMode(player)) {
            disable(player);
            return;
        }
        enable(player);
    }

    public boolean enable(Player player) {
        PaperChatUtil.send(player, GameMessages.editorMode_enabled);
        new EditorModeHotbar(player).set(player);
        register(player.getUniqueId(), new EditorSession(player));
        return true;
    }

    public boolean disable(Player player) {
        PaperChatUtil.send(player, GameMessages.editorMode_disabled);
        CorePlayer corePlayer = CorePlayer.get(player);

        if (corePlayer.getCurrentMenuSet() instanceof EditorModeHotbar editorModeHotbar) {
            editorModeHotbar.restore(player);
        }
        unregister(player.getUniqueId());
        return false;
    }

    public boolean hasEditorMode(Player player) {
        return keySet().contains(player.getUniqueId());
    }

    @Override
    public void load() {

    }

    @Override
    public void unload() {

    }
}
