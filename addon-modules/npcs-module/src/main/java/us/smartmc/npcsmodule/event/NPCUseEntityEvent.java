package us.smartmc.npcsmodule.event;

import com.comphenix.protocol.wrappers.EnumWrappers;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.smartmc.core.SmartCore;
import us.smartmc.npcsmodule.instance.CustomNPC;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

@Getter
public class NPCUseEntityEvent extends NPCEvent {

    private static final Set<Integer> firstAttackDelay = new HashSet<>();

    private final Player player;
    private final EnumWrappers.EntityUseAction action;

    public NPCUseEntityEvent(CustomNPC npc, Player player, EnumWrappers.EntityUseAction action) {
        super(npc);
        this.player = player;
        this.action = action;

        if (npc.isVulnerable()) {
            if (action.equals(EnumWrappers.EntityUseAction.ATTACK)) {
                if (firstAttackDelay.contains(npc.getNpcPlayer().getId())) return;
                Bukkit.getScheduler().runTask(SmartCore.getPlugin(), npc::simulateAttack);
                firstAttackDelay.add(npc.getNpcPlayer().getId());
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        firstAttackDelay.remove(npc.getNpcPlayer().getId());
                    }
                }, 320);
            }
        }
    }

}
