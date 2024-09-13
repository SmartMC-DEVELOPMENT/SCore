package us.smartmc.npcsmodule.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedEnumEntityUseAction;
import me.imsergioh.pluginsapi.SpigotPluginsAPI;
import org.bukkit.scheduler.BukkitRunnable;
import us.smartmc.core.SmartCore;
import org.bukkit.Bukkit;
import us.smartmc.npcsmodule.event.NPCUseEntityEvent;
import us.smartmc.npcsmodule.instance.CustomNPC;
import us.smartmc.npcsmodule.manager.NPCManager;

import java.util.*;

public class PlayerClickListener extends PacketAdapter {

    private final Set<UUID> clicking = new HashSet<>();

    public PlayerClickListener() {
        super(SmartCore.getPlugin(), PacketType.Play.Client.USE_ENTITY);
        // Registra este listener con ProtocoLib
        ProtocolLibrary.getProtocolManager().addPacketListener(this);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        int id = event.getPacket().getIntegers().read(0);
        NPCManager.forEach(npcManager -> {
            CustomNPC npc = npcManager.getNPCByEntityId(id);
            if (npc == null) return;
            UUID uuid = event.getPlayer().getUniqueId();
            if (clicking.contains(uuid)) return;

            EnumWrappers.EntityUseAction action = event.getPacket().getEntityUseActions().read(0);  // Cambiado para 1.8

            clicking.add(uuid);

            // Ejecuta el evento en el siguiente tick de Bukkit
            new BukkitRunnable() {
                @Override
                public void run() {
                    Bukkit.getPluginManager().callEvent(new NPCUseEntityEvent(npc, event.getPlayer(), action));
                }
            }.runTask(SpigotPluginsAPI.getPlugin());

            // Programa la eliminación del jugador del set de "clicking" después de 75 ms
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    clicking.remove(uuid);
                }
            }, 75);
        });
    }
}

