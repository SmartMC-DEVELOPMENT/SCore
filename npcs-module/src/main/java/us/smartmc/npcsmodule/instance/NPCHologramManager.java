package us.smartmc.npcsmodule.instance;

import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.util.ChatUtil;
import me.imsergioh.pluginsapi.util.LanguageUtil;
import me.imsergioh.pluginsapi.util.LegacyChatUtil;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import us.smartmc.core.SmartCore;
import us.smartmc.smartaddons.plugin.AddonListener;

import java.util.*;


public class NPCHologramManager extends AddonListener implements Listener {

    private final CustomNPC npc;
    private final HashMap<Integer, ArmorStand> stands = new HashMap<>();
    private final HashMap<Integer, String> names = new HashMap<>();
    private final Set<Player> viewers = new HashSet<>();

    private final int updateTaskID;

    private final Map<Language, List<String>> constantLinesMap = new HashMap<>();

    public NPCHologramManager(CustomNPC npc) {
        this.npc = npc;
        updateTaskID = setupUpdateTask();
        Bukkit.getPluginManager().registerEvents(this, SmartCore.getPlugin());
    }

    private int setupUpdateTask() {
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(SmartCore.getPlugin(), () -> {
            if (viewers.isEmpty()) return;

            constantLinesMap.clear();
            // Register only changed lines in the map below
            for (Player player : viewers) {
                if (constantLinesMap.keySet().size() == Language.values().length) break;
                Language language = PlayerLanguages.get(player.getUniqueId());
                if (constantLinesMap.containsKey(language)) continue;
                List<String> linesList = getLines(language);

                constantLinesMap.put(language, linesList);
            }

            // Update holograms to players if changed
            for (Player player : viewers) {
                UUID uuid = player.getUniqueId();
                Language language = PlayerLanguages.get(uuid);
                if (!constantLinesMap.containsKey(language)) continue;
                updateHolograms(player, constantLinesMap.get(language));
            }
        }, 0, 20 * 3);
    }

    public void cancelUpdateTask() {
        Bukkit.getScheduler().cancelTask(updateTaskID);
    }

    public void setupStands() {
        List<String> lines = getLines();
        if (lines == null) return;
        if (lines.isEmpty()) return;

        Location location = npc.getLocation();
        location.add(0, -0.15, 0);

        for (int i = lines.size() - 1; i >= 0; i--) {
            String line = lines.get(i);
            ArmorStand stand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
            stand.setCustomName(line);
            stand.setCustomNameVisible(true);
            stand.setVisible(false);
            stand.setGravity(false);
            stand.setSmall(true);
            stands.put(i, stand);
            names.put(stand.getEntityId(), line);
            location.add(0, 0.3, 0);
        }
    }

    public void createHologram(Player player) {
        List<String> originalLines = getLines();
        if (originalLines == null) return;
        List<String> lines = new ArrayList<>(originalLines);
        if (lines.isEmpty()) return;
        for (int i = lines.size() - 1; i >= 0; i--) {
            ArmorStand stand = stands.get(i);
            spawnVisibleArmorStand(player, stand);
        }
    }

    private List<String> getLines(Language language) {
        List<String> list = new ArrayList<>();
        for (String line : getLines()) {
            String languageParsedLine = LanguageUtil.parse(language, line);
            list.add(LegacyChatUtil.parse(languageParsedLine));
        }
        return list;
    }

    public void updateHolograms(Player player, List<String> names) {
        for (int i = 0; i < stands.size(); i++) {
            ArmorStand stand = stands.get(i);
            String name = names.get(i);
            String parsedName = LegacyChatUtil.parse(player, name);

            stand.setCustomName(parsedName);

            PacketPlayOutEntityMetadata dataPacket = new PacketPlayOutEntityMetadata(stand.getEntityId(), List.of());
            ((CraftPlayer) player).getHandle().connection.send(dataPacket);
        }
    }

    public void spawnVisibleArmorStand(Player player, ArmorStand originalArmorStand) {
        String name = originalArmorStand.getName();
        // Obtener el EntityLiving del nuevo ArmorStand
        originalArmorStand.setCustomName(LegacyChatUtil.parse(player, name));

        Location loc = originalArmorStand.getLocation();

        originalArmorStand.teleport(loc);

        // Enviar un paquete para mostrar el nuevo ArmorStand solo al visibleArmorStandEntity
        HologramArmorStand hologramArmorStand = new HologramArmorStand(originalArmorStand);
        hologramArmorStand.spawn(player);

        updateHolograms(player, getLines(PlayerLanguages.get(player.getUniqueId())));
        viewers.add(player);
    }

    public void removeViewer(Player player) {
        viewers.remove(player);
    }

    public List<String> getLines() {
        return npc.getLines();
    }
}
