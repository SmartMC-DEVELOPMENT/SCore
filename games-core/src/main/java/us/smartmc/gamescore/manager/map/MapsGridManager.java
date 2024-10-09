package us.smartmc.gamescore.manager.map;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapsGridManager {

    @Getter
    private final MapsManager manager;

    // Blocks to add for coords x or z that are variable
    private final int ADDITION_PER_SESSION = 256;

    // X Coord that defines current index
    private int x = -(ADDITION_PER_SESSION);

    private final List<Integer> slotsAvailable = new ArrayList<>();

    public MapsGridManager(MapsManager mapsManager) {
        this.manager = mapsManager;
    }

    public Location getLocationReferencePoint(World world) {
        return new Location(world, getNewSlotIndex(), 70, new Random().nextInt(1000));
    }

    private int getNewSlotIndex() {
        if (!slotsAvailable.isEmpty()) {
            int index = slotsAvailable.get(0);
            slotsAvailable.remove(0);
            return index;
        }
        x += ADDITION_PER_SESSION;
        return x;
    }

    public void setAvailableIndex(int x) {
        if (slotsAvailable.contains(x)) return;
        slotsAvailable.add(x);
    }

}
