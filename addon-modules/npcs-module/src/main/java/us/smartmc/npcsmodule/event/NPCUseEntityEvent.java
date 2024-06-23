package us.smartmc.npcsmodule.event;

import com.comphenix.protocol.wrappers.EnumWrappers;
import lombok.Getter;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import us.smartmc.npcsmodule.instance.CustomNPC;

@Getter
public class NPCUseEntityEvent extends NPCEvent {

    private final Player player;
    private final EnumWrappers.EntityUseAction action;

    public NPCUseEntityEvent(CustomNPC npc, Player player, EnumWrappers.EntityUseAction action) {
        super(npc);
        this.player = player;
        this.action = action;
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.6F, 2F);
    }

}
