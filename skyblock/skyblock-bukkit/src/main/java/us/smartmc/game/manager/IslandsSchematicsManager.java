package us.smartmc.game.manager;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.*;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import lombok.Getter;
import me.imsergioh.pluginsapi.util.SyncUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.joml.Vector3d;
import us.smartmc.game.SkyBlockPlugin;
import us.smartmc.game.instance.DefaultIsland;
import us.smartmc.game.instance.EmptyChunkGenerator;
import us.smartmc.game.instance.SkyBlockPlayerIsland;
import us.smartmc.skyblock.instance.island.ISkyBlockIsland;
import us.smartmc.skyblock.manager.IslandsManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class IslandsSchematicsManager {

    private static final File ISLANDS_DIRECTORY = new File("/home/network/data-skyblock/islands");

    @Getter
    private static final UUID defaultIslandId = UUID.fromString("e3d71b5a-2f63-4084-abe4-f03cb33258bd");

    private static World nextWorld;

    public static void registerDefaults() {
        ISLANDS_DIRECTORY.mkdirs();
        IslandsManager.register(new DefaultIsland(defaultIslandId));
        nextWorld = createIslandWorld();
    }

    public static File getMapSchematicFile(UUID islandId) {
        return new File(ISLANDS_DIRECTORY, islandId.toString() + ".schem");
    }

    public static void saveRegion(World world, ISkyBlockIsland island) throws Exception {
        com.sk89q.worldedit.world.World weWorld = WorldEdit.getInstance().getPlatformManager().getWorldForEditing(new BukkitWorld(world));

        BlockVector3 pos1 = getBlockVectorByLocation(island.getIslandData().getMinLocation(world));
        BlockVector3 pos2 = getBlockVectorByLocation(island.getIslandData().getMaxLocation(world));
        BlockVector3 min = min(pos1, pos2);
        BlockVector3 max = max(pos1, pos2);

        // Validar que las coordenadas están dentro del mundo
        /*if (!weWorld.isValidLocation(min) || !weWorld.isValidLocation(max) || !weWorld.isValidLocation(center)) {
            throw new IllegalArgumentException("Las coordenadas de la región están fuera de los límites válidos del mundo.");
        }*/

        CuboidRegion region = new CuboidRegion(weWorld, min, max);

        // Crear el portapapeles para copiar
        BlockArrayClipboard clipboard = new BlockArrayClipboard(region);

        // Realizar la copia
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(weWorld)) {
            ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(
                    editSession, region, clipboard, min
            );
            Operations.complete(forwardExtentCopy);
        }

        File file = getMapSchematicFile(island.getId());

        // Guardar el portapapeles en un archivo schematic
        try (ClipboardWriter writer = BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(new FileOutputStream(file))) {
            writer.write(clipboard);
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Error al guardar el archivo schematic.", e);
        }
    }

    private static BlockVector3 getBlockVectorByLocation(Vector3d vector3d) {
        return BlockVector3.at(vector3d.x(), vector3d.y(), vector3d.z());
    }

    public static void loadAndPasteSchematic(World world, UUID islandId) {
        try {
            Clipboard clipboard;
            File file = getMapSchematicFile(islandId);
            ClipboardFormat format = ClipboardFormats.findByFile(file);
            try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
                clipboard = reader.read();
            }

            SkyBlockPlayerIsland island = (SkyBlockPlayerIsland) IslandsManager.get(islandId);

            BlockVector3 pos1 = getBlockVectorByLocation(island.getIslandData().getMinLocation(world));
            BlockVector3 pos2 = getBlockVectorByLocation(island.getIslandData().getMaxLocation(world));
            BlockVector3 min = min(pos1, pos2);

            try (EditSession editSession = WorldEdit.getInstance().newEditSession(new BukkitWorld(world))) {
                Operation operation = new ClipboardHolder(clipboard)
                        .createPaste(editSession)
                        .to(min)
                        .build();
                Operations.complete(operation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Location getSpawnLocation(World world, UUID islandId) {
        SkyBlockPlayerIsland island = (SkyBlockPlayerIsland) us.smartmc.skyblock.manager.IslandsManager.get(islandId);
        return island.getIslandData().getSpawnLocation(world);
    }

    public static BlockVector3 max(BlockVector3 vec1, BlockVector3 vec2) {
        int maxX = Math.max(vec1.getBlockX(), vec2.getBlockX());
        int maxY = Math.max(vec1.getBlockY(), vec2.getBlockY());
        int maxZ = Math.max(vec1.getBlockZ(), vec2.getBlockZ());
        return BlockVector3.at(maxX, maxY, maxZ);
    }

    public static BlockVector3 min(BlockVector3 vec1, BlockVector3 vec2) {
        int minX = Math.min(vec1.getBlockX(), vec2.getBlockX());
        int minY = Math.min(vec1.getBlockY(), vec2.getBlockY());
        int minZ = Math.min(vec1.getBlockZ(), vec2.getBlockZ());
        return BlockVector3.at(minX, minY, minZ);
    }

    public static World getNextIslandWorld() {
        prepareNextIslandWorld();
        return nextWorld;
    }

    public static void prepareNextIslandWorld() {
        SyncUtil.later(() -> {
            Bukkit.getScheduler().runTask(SkyBlockPlugin.getPlugin(), () -> {
                nextWorld = createIslandWorld();
            });
        }, 25);
    }

    private static World createIslandWorld() {
        WorldCreator worldCreator = new WorldCreator(getIslandWorldName(UUID.randomUUID()));
        worldCreator.generator(new EmptyChunkGenerator());
        return worldCreator.createWorld();
    }

    public static World getDefaultIslandWorld() {
        World w = Bukkit.getWorld(getIslandWorldName(defaultIslandId));
        if (w != null) return w;
        WorldCreator worldCreator = new WorldCreator(getIslandWorldName(defaultIslandId));
        worldCreator.generator(new EmptyChunkGenerator());
        return worldCreator.createWorld();
    }

    protected static String getIslandWorldName(UUID id) {
        return "island-" + id.toString();
    }

}
