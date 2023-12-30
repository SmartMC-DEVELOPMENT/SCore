package us.smartmc.core.regions.controller;

import lombok.Getter;
import org.bukkit.entity.Player;
import us.smartmc.core.regions.Cuboid;

public class CuboidEditorPlayerSession {

    @Getter
    private final Player player;

    private Cuboid editorCuboid;

    public CuboidEditorPlayerSession(Player player) {
        this.player = player;
    }

    public Cuboid getEditorCuboid() {
        if (editorCuboid == null) editorCuboid = new Cuboid();
        return editorCuboid;
    }
}