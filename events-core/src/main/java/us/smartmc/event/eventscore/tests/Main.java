package us.smartmc.event.eventscore.tests;

import us.smartmc.event.eventscore.types.EventWhitelistType;

import java.lang.reflect.Field;

public class Main {


    public static void main(String[] args) throws Exception {
        Field field = EventWhitelistType.class.getDeclaredField("id");
        field.setAccessible(true);
        for (EventWhitelistType whitelistType : EventWhitelistType.values()) {
            System.out.println(field.get(whitelistType));
        }
    }

}
