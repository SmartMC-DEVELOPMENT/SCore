package us.smartmc.npcsmodule.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import us.smartmc.core.SmartCore;
import org.bukkit.Bukkit;
import us.smartmc.npcsmodule.NPCSModule;
import us.smartmc.npcsmodule.event.NPCUseEntityEvent;
import us.smartmc.npcsmodule.instance.NPC;
import us.smartmc.npcsmodule.manager.NPCManager;

public class PlayerClickListener extends PacketAdapter {

    public PlayerClickListener() {
        super(SmartCore.getPlugin(), PacketType.Play.Client.USE_ENTITY);
        // Registra este listener con ProtocoLib
        ProtocolLibrary.getProtocolManager().addPacketListener(this);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        int id = event.getPacket().getIntegers().read(0);
        NPCManager.forEach(npcManager -> {
            NPC npc = npcManager.getNPC(id);
            if (npc == null) return;
            EnumWrappers.EntityUseAction action = event.getPacket().getEntityUseActions().read(0);
            Bukkit.getPluginManager()
                    .callEvent(new NPCUseEntityEvent(npc, event.getPlayer(), action));
        });

    }
}

