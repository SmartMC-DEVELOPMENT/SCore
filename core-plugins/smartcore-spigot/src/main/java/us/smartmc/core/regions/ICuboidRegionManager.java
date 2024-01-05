package us.smartmc.core.regions;

import java.util.List;

public interface ICuboidRegionManager {

    void setCuboidRegion(Cuboid cuboidRegion);
    void setPriority(int priority);
    void addRegionMeta(String metaString);

    String getName();
    Cuboid getCuboidRegion();
    int getPriority();
    List<String> getRegionMetas();

}