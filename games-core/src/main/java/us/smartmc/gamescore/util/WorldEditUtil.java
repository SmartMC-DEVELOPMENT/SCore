package us.smartmc.gamescore.util;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.adapter.BukkitImplAdapter;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.session.PasteBuilder;
import com.sk89q.worldedit.world.registry.WorldData;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.*;
import java.util.Objects;
import java.util.function.Consumer;

public class WorldEditUtil {

    public static void paste(Location location, File file, Consumer<PasteBuilder> optionsConsumer) throws IOException, WorldEditException {
        Clipboard clipboard = loadFromFile(location.getWorld(), file);
        if (clipboard == null) {
            throw new IOException("Failed to load clipboard");
        }
        com.sk89q.worldedit.world.World weWorld = BukkitUtil.getLocalWorld(location.getWorld());
        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(weWorld, -1);
        PasteBuilder holder = new ClipboardHolder(clipboard, weWorld.getWorldData())
                .createPaste(editSession, weWorld.getWorldData())
                .to(new Vector(location.getBlockX(), location.getBlockY(), location.getBlockZ()))
                .ignoreAirBlocks(false);
        optionsConsumer.accept(holder);
        Operation operation = holder.build();
        Operations.complete(operation);
    }

    private static Clipboard loadFromFile(World world, File file) throws IOException {
        WorldData worldData = BukkitUtil.getLocalWorld(world).getWorldData();
        ClipboardFormat format = ClipboardFormat.findByFile(file);
        if (format == null) return null;
        ClipboardReader reader = format.getReader(new FileInputStream(file));
        return reader.read(worldData);
    }

    public static BlockArrayClipboard getClipboard(Location corner1, Location corner2) {
        com.sk89q.worldedit.world.World weWorld = BukkitUtil.getLocalWorld(corner1.getWorld());
        Vector min = getMinVector(corner1, corner2);
        Vector max = getMaxVector(corner1, corner2);
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

    public static Vector getMaxVector(Location l1, Location l2) {
        Vector v1 = getVectorFromLocation(l1);
        Vector v2 = getVectorFromLocation(l2);
        return Vector.getMaximum(v1, v2);
    }

    public static Vector getMinVector(Location l1, Location l2) {
        Vector v1 = getVectorFromLocation(l1);
        Vector v2 = getVectorFromLocation(l2);
        return Vector.getMinimum(v1, v2);
    }

    public static Vector getVectorFromLocation(Location loc) {
        return new Vector(loc.getX(), loc.getY(), loc.getZ());
    }

}
