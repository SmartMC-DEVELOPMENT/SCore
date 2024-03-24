package us.smartmc.npcsmodule.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import us.smartmc.npcsmodule.instance.CustomNPC;

public class NPCEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final CustomNPC npc;

    public NPCEvent(CustomNPC npc) {
        this.npc = npc;
    }

    public CustomNPC getNPC() {
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
