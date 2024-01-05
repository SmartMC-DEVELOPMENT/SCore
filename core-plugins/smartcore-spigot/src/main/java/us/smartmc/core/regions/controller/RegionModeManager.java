package us.smartmc.core.regions.controller;

import me.imsergioh.pluginsapi.instance.manager.ManagerRegistry;
import org.bukkit.entity.Player;
import us.smartmc.core.SmartCore;

import java.util.UUID;

public class RegionModeManager extends ManagerRegistry<UUID, CuboidEditorPlayerSession> {

    private static final SmartCore plugin = SmartCore.getPlugin();

    public boolean toggleMode(Player player) {
        UUID id = player.getUniqueId();
        boolean isToggled = isRegistered(id);
        if (!isToggled) {
            register(id, new CuboidEditorPlayerSession(player));
        } else {
            unregister(id);
        }
        return !isToggled;
    }

    @Override
    public void load() {

    }

    @Override
    public void unload() {

    }

}