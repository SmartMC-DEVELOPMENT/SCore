package us.smartmc.npcsmodule.util;


import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.world.entity.Entity;

public class NMSUtils {

    public static DataWatcher cloneDataWatcher(Entity entity) {
        return new DataWatcher(entity);
    }

}
