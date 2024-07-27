package us.smartmc.game.luckytowers.instance.player;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.schematic.SchematicFormat;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import us.smartmc.game.luckytowers.LuckyTowers;
import us.smartmc.game.luckytowers.instance.game.GameMap;
import us.smartmc.game.luckytowers.instance.game.GameTeamColor;
import us.smartmc.game.luckytowers.manager.GameMapManager;

import java.io.File;
import java.io.IOException;

public class EditorSession {

    @Getter
    private final Player player;

    private static final GameMapManager mapManager = LuckyTowers.getManager(GameMapManager.class);

    private GameTeamColor color = GameTeamColor.values()[0];

    @Setter
    @Getter
    private String mapId;

    public EditorSession(Player player) {
        this.player = player;
    }

    public void saveRegion(org.bukkit.World bukkitWorld, File schematicFile) throws Exception {
        try {
            // Convert BukkitWorld to LocalWorld
            LocalWorld localWorld = new BukkitWorld(bukkitWorld);

            // Get the selection from WorldEdit
            WorldEdit worldEdit = WorldEditPlugin.getPlugin(WorldEditPlugin.class).getWorldEdit();
            Selection selection = WorldEditPlugin.getPlugin(WorldEditPlugin.class).getSelection(player);
            Vector pos1 = selection.getNativeMaximumPoint();
            Vector pos2 = selection.getNativeMinimumPoint();

            if (selection == null) {
                throw new Exception("No selection found.");
            }

            // Calculate the center of the selection
            Vector center = pos1.add(pos2).multiply(0.5);

            // Create a Schematic
            CuboidClipboard schem = new CuboidClipboard(pos1, pos2);

            // Adjust the schematic to be centered
            schem.setOffset(center);

            // Create an EditSession
            EditSession editSession = new EditSession(localWorld, -1);

            // Copy the blocks from the region to the schematic
            schem.copy(editSession);

            // Save the schematic to a file
            SchematicFormat.MCEDIT.save(schem, schematicFile);

            // Flush changes to the world
            editSession.flushQueue();

        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Error saving schematic file.", e);
        }
    }

    public GameTeamColor rotateColor() {
        return color.next();
    }

    public GameMap getMap() {
        return mapManager.get(mapId);
    }


    public static File getMapSchematicFile(String name) {
        return new File(GameMapManager.MAPS_SCHEMS_DIRECTORY, name + ".schematic");
    }
}
