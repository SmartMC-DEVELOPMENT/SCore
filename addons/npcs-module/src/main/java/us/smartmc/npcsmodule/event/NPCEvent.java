package us.smartmc.npcsmodule.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import us.smartmc.npcsmodule.instance.NPC;

public class NPCEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final NPC npc;

    public NPCEvent(NPC npc) {
        this.npc = npc;
    }

    public NPC getNPC() {
        return npc;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
