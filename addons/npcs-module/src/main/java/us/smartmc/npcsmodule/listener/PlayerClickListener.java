package us.smartmc.npcsmodule.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
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
            CustomNPC npc = npcManager.getNPC(id);
            if (npc == null) return;
            UUID uuid = event.getPlayer().getUniqueId();
            if (clicking.contains(uuid)) return;
            EnumWrappers.EntityUseAction action = event.getPacket().getEntityUseActions().read(0);
            clicking.add(uuid);
            Bukkit.getPluginManager()
                    .callEvent(new NPCUseEntityEvent(npc, event.getPlayer(), action));
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    clicking.remove(uuid);
                }
            }, 75);
        });

    }
}

