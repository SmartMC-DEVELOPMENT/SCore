package us.smartmc.npcsmodule.instance;

import org.bukkit.Location;

import java.util.Timer;
import java.util.TimerTask;

public class NPCGravitySimulationTask implements Runnable {

    // 50 MILLIS = 1 TICK
    private static final int DELAY = 25;

    private final CustomNPC npc;

    public NPCGravitySimulationTask(CustomNPC npc) {
        this.npc = npc;
    }

    @Override
    public void run() {
        Location npcLocation = npc.getLocation();
        Location belowLocation = npcLocation.clone().subtract(0, 0.05, 0);
        while (belowLocation.getBlock().isEmpty()) {
            try {
                // Wait 4 ticks for next iteration
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // No hay bloque debajo, aplica gravedad
            npcLocation.setY(npcLocation.getY() - 0.05); // Ajusta la velocidad de caída según sea necesario
            npc.teleportTo(npcLocation);
            npcLocation = npc.getLocation();
            belowLocation = npcLocation.clone().subtract(0, 0.05, 0);
        }

        npcLocation = npc.getLocation();
        belowLocation = npcLocation.clone().subtract(0, 0.05, 0);
        if (belowLocation.getBlock().isEmpty()) {
            try {
                // Wait 4 ticks for next iteration
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // No hay bloque debajo, aplica gravedad
            npcLocation.setY(npcLocation.getY() - 0.05); // Ajusta la velocidad de caída según sea necesario
            npc.teleportTo(npcLocation);
        }
        npc.resetGravitySimulationTask();
    }
}
