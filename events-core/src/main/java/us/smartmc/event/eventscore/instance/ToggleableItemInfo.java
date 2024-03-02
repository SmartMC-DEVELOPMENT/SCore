package us.smartmc.event.eventscore.instance;

import org.bukkit.Material;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ToggleableItemInfo {

    String name();
    Material material();

}
