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
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;

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

    public void saveRegion(Player player, File toSaveSchemFile) throws Exception {
        try {
            String name = toSaveSchemFile.getName().split("\\.")[0];
            player.performCommand("/copy");
            player.performCommand("/schem save " + name);

            File originalFile = new File(LuckyTowers.getPlugin().getDataFolder() + "/../WorldEdit/schematics/" + name + ".schematic");
            toSaveSchemFile.delete();
            Files.copy(Path.of(originalFile.toURI()), Path.of(toSaveSchemFile.toURI()));
            player.sendMessage("Saved to " + toSaveSchemFile.getAbsolutePath() + "!");
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
