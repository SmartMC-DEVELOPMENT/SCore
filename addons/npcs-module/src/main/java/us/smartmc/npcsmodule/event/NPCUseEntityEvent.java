package us.smartmc.npcsmodule.event;

import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import us.smartmc.npcsmodule.instance.NPC;

public class NPCUseEntityEvent extends NPCEvent {

    private final Player player;
    private final EnumWrappers.EntityUseAction action;

    public NPCUseEntityEvent(NPC npc, Player player, EnumWrappers.EntityUseAction action) {
        super(npc);
        this.player = player;
        this.action = action;
        player.playSound(player.getLocation(), Sound.CLICK, 0.6F, 2F);
    }

    public Player getPlayer() {
        return player;
    }

    public EnumWrappers.EntityUseAction getAction() {
        return action;
    }
}
