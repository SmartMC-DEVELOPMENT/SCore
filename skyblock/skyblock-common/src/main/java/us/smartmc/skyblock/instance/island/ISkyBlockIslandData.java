package us.smartmc.skyblock.instance.island;


import org.joml.Vector3d;

public interface ISkyBlockIslandData {

    Vector3d getMinLocation(Object world);
    Vector3d getMaxLocation(Object world);

}
