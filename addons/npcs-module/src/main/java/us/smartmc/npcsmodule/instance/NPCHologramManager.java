package us.smartmc.npcsmodule.instance;

import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.util.ChatUtil;
import me.imsergioh.pluginsapi.util.LanguageUtil;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import us.smartmc.core.SmartCore;
import us.smartmc.npcsmodule.util.NMSUtils;
import us.smartmc.smartaddons.plugin.AddonListener;

import java.util.*;


public class NPCHologramManager extends AddonListener implements Listener {

    private final NPC npc;
    private final HashMap<Integer, EntityArmorStand> stands = new HashMap<>();
    private final HashMap<Integer, String> names = new HashMap<>();
    private final Set<Player> viewers = new HashSet<>();

    private final int updateTaskID;

    private final Map<Language, List<String>> constantLinesMap = new HashMap<>();
    private final Map<Language, Integer> lastKnownLines = new HashMap<>();

    public NPCHologramManager(NPC npc) {
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

                // Calcula el hash de las líneas actuales
                int currentLinesHash = linesList.hashCode();

                // Compara el hash actual con el último hash conocido
                Integer lastKnownLinesHash = lastKnownLines.containsKey(language) ? lastKnownLines.get(language).hashCode() : null;
                if (lastKnownLinesHash != null && currentLinesHash == lastKnownLinesHash) continue;

                constantLinesMap.put(language, linesList);
                // Actualiza el hash en lastKnownLines
                lastKnownLines.put(language, currentLinesHash); // Considera almacenar el hash directamente para mejorar la eficiencia
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
        location.add(0, 0.92, 0);

        for (int i = lines.size() - 1; i >= 0; i--) {
            String line = lines.get(i);
            EntityArmorStand stand = new EntityArmorStand(null, location.getX(), location.getY(), location.getZ());
            stand.setCustomName(line);
            stand.setCustomNameVisible(true);
            stand.setInvisible(true);
            stand.setGravity(false);
            stand.setSmall(true);
            stands.put(i, stand);
            names.put(stand.getId(), stand.getCustomName());
            location.add(0, 0.3, 0);
        }
    }

    public void createHologram(Player player) {
        List<String> originalLines = getLines();
        if (originalLines == null) return;
        List<String> lines = new ArrayList<>(originalLines);
        if (lines.isEmpty()) return;
        for (int i = lines.size() - 1; i >= 0; i--) {
            EntityArmorStand stand = stands.get(i);
            spawnVisibleArmorStand(player, stand);
        }
    }

    private List<String> getLines(Language language) {
        List<String> list = new ArrayList<>();
        for (String line : getLines()) {
            String languageParsedLine = LanguageUtil.parse(language, line);
            list.add(ChatUtil.parse(languageParsedLine));
        }
        return list;
    }

    public void updateHolograms(Player player, List<String> lines) {
        if (player == null) return;
        for (int i = lines.size() - 1; i >= 0; i--) {
            EntityArmorStand stand = stands.get(i);

            String name = names.get(stand.getId());
            String parsedName = ChatUtil.parse(player, name);
            lines.set(i, parsedName);

            DataWatcher newWatcher = NMSUtils.cloneDataWatcher(stand);
            newWatcher.a(2, parsedName);
            newWatcher.update(2);
            PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(stand.getId(), newWatcher, true);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
    }

    public void spawnVisibleArmorStand(Player player, EntityArmorStand originalArmorStand) {
        CraftArmorStand visibleArmorStand = new CraftArmorStand(((CraftServer) Bukkit.getServer()).getHandle().getServer().server, originalArmorStand);

        // Obtener el EntityLiving del nuevo ArmorStand
        EntityLiving visibleArmorStandEntity = visibleArmorStand.getHandle();
        String name = names.get(originalArmorStand.getId());
        originalArmorStand.setCustomName(ChatUtil.parse(player, name));

        // Enviar un paquete para mostrar el nuevo ArmorStand solo al jugador
        PacketPlayOutSpawnEntityLiving packetPlayOutSpawnEntityLiving = new PacketPlayOutSpawnEntityLiving(visibleArmorStandEntity);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutSpawnEntityLiving);

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
