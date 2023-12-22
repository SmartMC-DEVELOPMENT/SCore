package us.smartmc.npcsmodule.util;

import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.Entity;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class NMSUtils {

    public static DataWatcher cloneDataWatcher(Entity entity) {

        DataWatcher originalWatcher = entity.getDataWatcher();
        DataWatcher newWatcher = new DataWatcher(entity); // Create a new DataWatcher instance

        HashMap<Integer, DataWatcher.WatchableObject> originalData = new HashMap<>();

        try {
            for (Field field : DataWatcher.class.getDeclaredFields()) {
                if (field.getName().equals("b")) {
                    field.setAccessible(true);
                    Object value = field.get(originalWatcher);
                    if (value instanceof Map) {
                        originalData.putAll((Map<? extends Integer, ? extends DataWatcher.WatchableObject>) value);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int num : originalData.keySet()) {
            newWatcher.watch(num, originalData.get(num));
        }
        return newWatcher;
    }

}
