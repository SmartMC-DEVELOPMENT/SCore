package us.smartmc.addon.holograms.instance.hologram;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class HologramBuilder {

    private final Hologram hologram;

    private HologramBuilder(String hologramName, Location location) {
        this.hologram = new Hologram(hologramName, location);
    }

    public Hologram build() {
        return hologram;
    }

    public HologramBuilder separator(double separator) {
        hologram.setLineSeparation(separator);
        return this;
    }

    public HologramBuilder lines(String... lines) {
        for (String line : lines) {
            hologram.addLine(line);
        }
        return this;
    }

    public HologramBuilder location(Location location) {
        hologram.setLocation(location);
        return this;
    }

    public HologramBuilder registerToHolder(HologramHolder holder) {
        holder.registerHologram(hologram);
        return this;
    }

    public HologramBuilder saveConfigurableToHolder(HologramHolder holder) {
        holder.registerConfigurableHologram(hologram.getName(), hologram.getLocation(), getLines());
        return this;
    }

    private List<String> getLines() {
        List<String> lines = new ArrayList<>();
        hologram.getLinesStands().forEach(hologramArmorStand -> {
            lines.add(hologramArmorStand.getUnformattedLine());
        });
        return lines;
    }

    public static HologramBuilder create(String hologramName, Location location) {
        return new HologramBuilder(hologramName, location);
    }

}
