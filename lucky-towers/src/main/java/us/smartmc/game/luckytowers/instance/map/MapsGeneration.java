package us.smartmc.game.luckytowers.instance.map;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import us.smartmc.game.luckytowers.LuckyTowers;
import us.smartmc.game.luckytowers.instance.game.GameSession;
import us.smartmc.game.luckytowers.instance.player.EditorSession;
import us.smartmc.game.luckytowers.manager.GameMapManager;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Set;

public class MapsGeneration {

    private static final GameMapManager manager = LuckyTowers.getManager(GameMapManager.class);
    private static final int X_ADDITION = 640;

    private final Location initLocation;
    private final int x;

    private Set<Integer> reserved = new HashSet<>();

    public MapsGeneration(World world) {
        initLocation = world.getSpawnLocation();
        x = (int) initLocation.getX();
    }

    public void setAvailable(int xCoord) {
        reserved.remove(xCoord);
    }

    public Chunk reserveNext() {
        int nextXLocation = getNextXLoc();
        reserved.add(nextXLocation);
        return manager.getWorld().getChunkAt(nextXLocation, initLocation.getBlockZ());
    }

    private int getNextXLoc() {
        int xLoc = x;
        while (reserved.contains(xLoc)) {
            xLoc += X_ADDITION;
        }
        return xLoc;
    }

    public static int getXAdditionByChunk(Chunk chunkOfMap, Chunk chunkOfSession) {
        return Math.max(chunkOfMap.getX(), chunkOfSession.getX()) - Math.min(chunkOfMap.getX(), chunkOfSession.getX());
    }

    public static EditSession loadAndPasteSchematic(GameSession session) {
        try {
            Clipboard clipboard;
            File file = EditorSession.getMapSchematicFile(session.getMap().getName());
            ClipboardFormat format = ClipboardFormats.findByFile(file);
            try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
                clipboard = reader.read();
            }
            int xAddition = session.getXAddition();
            World bukkitWorld = session.getMap().getSpawn(0).getWorld();

            Location center = session.getMap().getSpawn(xAddition);
            int x = center.getBlockX();
            int y = center.getBlockY();
            int z = center.getBlockZ();

            try (EditSession editSession = WorldEdit.getInstance().newEditSession(new BukkitWorld(bukkitWorld))) {
                Operation operation = new ClipboardHolder(clipboard)
                        .createPaste(editSession)
                        .to(BlockVector3.at(x, y, z))
                        // configure here
                        .build();
                Operations.complete(operation);
                return editSession;
            }
        } catch (Exception e) {e.printStackTrace();}
        return null;
    }

}
