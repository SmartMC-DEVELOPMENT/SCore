package us.smartmc.gamescore.util;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;

public class WorldEditUtil {

    public static BlockArrayClipboard getClipboard(Location corner1, Location corner2) {
        com.sk89q.worldedit.world.World weWorld = BukkitUtil.getLocalWorld(corner1.getWorld());
        Vector min = getMinFromLocs(corner1, corner2);
        Vector max = getMaxFromLocs(corner1, corner2);
        CuboidRegion region = new CuboidRegion(weWorld, min, max);
        return new BlockArrayClipboard(region);
    }

    public static ForwardExtentCopy copyRegion(Location corner1, Location corner2, Consumer<ForwardExtentCopy> optionsConsumer) {
        World world = corner1.getWorld();
        BlockArrayClipboard clipboard = getClipboard(corner1, corner2);
        Region region = clipboard.getRegion();
        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(region.getWorld(), -1);

        ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(editSession, region, clipboard, region.getMinimumPoint());

        if (optionsConsumer != null) optionsConsumer.accept(forwardExtentCopy);

        try {
            Operations.complete(forwardExtentCopy);
        } catch (WorldEditException e) {
            throw new RuntimeException(e);
        }
        return forwardExtentCopy;
    }

    public static void saveSchematic(Location corner1, Location corner2, File file, Consumer<ForwardExtentCopy> optionsConsumer) {
        ForwardExtentCopy forwardExtentCopy = copyRegion(corner1, corner2, optionsConsumer);
        BlockArrayClipboard clipboard = getClipboard(corner1, corner2);
        try {
            Operations.complete(forwardExtentCopy);
            try (ClipboardWriter writer = ClipboardFormat.SCHEMATIC.getWriter(new FileOutputStream(file))) {
                writer.write(clipboard, Objects.requireNonNull(clipboard.getRegion().getWorld()).getWorldData());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (WorldEditException e) {
            throw new RuntimeException(e);
        }
    }

    public static Vector getMaxFromLocs(Location l1, Location l2) {
        Vector v1 = getVectorFromLocation(l1);
        Vector v2 = getVectorFromLocation(l2);
        return Vector.getMaximum(v1, v2);
    }

    public static Vector getMinFromLocs(Location l1, Location l2) {
        Vector v1 = getVectorFromLocation(l1);
        Vector v2 = getVectorFromLocation(l2);
        return Vector.getMinimum(v1, v2);
    }

    public static Vector getVectorFromLocation(Location loc) {
        return new Vector(loc.getX(), loc.getY(), loc.getZ());
    }

}
