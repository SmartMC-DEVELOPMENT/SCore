package us.smartmc.game.luckytowers.instance.map;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.schematic.SchematicFormat;
import org.bukkit.Location;
import org.bukkit.World;
import us.smartmc.game.luckytowers.LuckyTowers;
import us.smartmc.game.luckytowers.instance.game.GameSession;
import us.smartmc.game.luckytowers.instance.player.EditorSession;
import us.smartmc.game.luckytowers.manager.GameMapManager;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class MapsGeneration {

    private static final GameMapManager manager = LuckyTowers.getManager(GameMapManager.class);
    private static final int X_ADDITION = 640;

    private final Set<Integer> reserved = new HashSet<>();

    public void setAvailable(int xCoord) {
        reserved.remove(xCoord);
    }

    public int reserveNext() {
        int nextXLocation = getNextXLoc();
        reserved.add(nextXLocation);
        return nextXLocation;
    }

    private int getNextXLoc() {
        int xLoc = 0;
        while (reserved.contains(xLoc)) {
            xLoc += X_ADDITION;
        }
        return xLoc;
    }

    public static EditSession loadAndPasteSchematic(World world, GameSession session) {
        EditSession editSession = null;
        File schematicFile = EditorSession.getMapSchematicFile(session.getMap().getName());

        try {
            // Cargar el schematic
            CuboidClipboard schem = SchematicFormat.MCEDIT.load(schematicFile);
            if (schem == null) {
                throw new IOException("No se pudo cargar el schematic.");
            }

            int xAddition = session.getXAddition();

            // Crear una sesión de edición para el mundo
            editSession = new EditSession(new BukkitWorld(world), Integer.MAX_VALUE);

            // Leer y pegar el schematic
            schem.paste(editSession, new Vector(xAddition, 70, 0), false);

            System.out.println("Pasted schematic at: " + new Vector(xAddition, 70, 0));

            // Guardar los cambios
            editSession.flushQueue();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return editSession;
    }

}
