package us.smartmc.core.util;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerVersions {

    private static final Map<UUID, Integer> registry = new HashMap<>();

    public static void unregister(UUID uuid) {
        registry.remove(uuid);
    }

    public static int get(UUID uuid) {
        return -1;
    }
}
