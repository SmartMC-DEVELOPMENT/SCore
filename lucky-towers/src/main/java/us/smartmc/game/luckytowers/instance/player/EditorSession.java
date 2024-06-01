package us.smartmc.game.luckytowers.instance.player;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.World;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import us.smartmc.game.luckytowers.LuckyTowers;
import us.smartmc.game.luckytowers.instance.game.GameMap;
import us.smartmc.game.luckytowers.instance.game.GameTeamColor;
import us.smartmc.game.luckytowers.manager.GameMapManager;

import java.io.File;
import java.io.FileOutputStream;

public class EditorSession {

    @Getter
    private final Player player;

    private static final GameMapManager mapManager = LuckyTowers.getManager(GameMapManager.class);

    private GameTeamColor color = GameTeamColor.values()[0];

    @Setter @Getter
    private String mapId;

    public EditorSession(Player player) {
        this.player = player;
    }

    public void saveRegion(org.bukkit.World world) throws Exception {
        World weWorld = WorldEdit.getInstance().getPlatformManager().getWorldForEditing(new BukkitWorld(player.getWorld()));
        BlockVector3 pos1 = getBlockVectorByLocation(getMap().getPos1(world, 0));
        BlockVector3 pos2 = getBlockVectorByLocation(getMap().getPos2(world, 0));

        BlockVector3 min = min(pos1, pos2);
        BlockVector3 max = max(pos1, pos2);

        CuboidRegion region = new CuboidRegion(weWorld, min, max);

        BlockArrayClipboard clipboard = new BlockArrayClipboard(region);

        ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(
                weWorld, region, clipboard, min
        );
        Operations.complete(forwardExtentCopy);

        File file = getMapSchematicFile(mapId);

        try (ClipboardWriter writer = BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(new FileOutputStream(file))) {
            writer.write(clipboard);
        }
    }

    public GameTeamColor rotateColor() {
        return color.next();
    }

    public GameMap getMap() {
        return mapManager.get(mapId);
    }

    public static BlockVector3 getBlockVectorByLocation(Location location) {
        int x = (int) location.getX();
        int y = (int) location.getY();
        int z = (int) location.getZ();
        return BlockVector3.at(x, y, z);
    }

    public static File getMapSchematicFile(String name) {
        return new File(GameMapManager.MAPS_SCHEMS_DIRECTORY, name + ".schem");
    }

    public static BlockVector3 max(BlockVector3 vec1, BlockVector3 vec2) {
        int maxX = Math.max(vec1.getX(), vec2.getX());
        int maxY = Math.max(vec1.getY(), vec2.getY());
        int maxZ = Math.max(vec1.getZ(), vec2.getZ());
        return BlockVector3.at(maxX, maxY, maxZ);
    }

    public static BlockVector3 min(BlockVector3 vec1, BlockVector3 vec2) {
        int minX = Math.min(vec1.getX(), vec2.getX());
        int minY = Math.min(vec1.getY(), vec2.getY());
        int minZ = Math.min(vec1.getZ(), vec2.getZ());
        return BlockVector3.at(minX, minY, minZ);
    }

}
