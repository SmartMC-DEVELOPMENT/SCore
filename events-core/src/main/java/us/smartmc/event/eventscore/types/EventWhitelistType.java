package us.smartmc.event.eventscore.types;

import us.smartmc.event.eventscore.EventsCore;
import us.smartmc.event.eventscore.instance.IToggleableType;
import us.smartmc.event.eventscore.util.ConfigPaths;

public enum EventWhitelistType implements IToggleableType<EventWhitelistType> {

    PUBLIC,
    NAME_LIST,
    ACCESS_CODE;

    @Override
    public EventWhitelistType get() {
        return EventsCore.getCore().getEventConfig().getEnumType(ConfigPaths.WHITELISTMODE_KEY, EventWhitelistType.class);
    }

    @Override
    public void set(EventWhitelistType value) {
        EventsCore.getCore().getEventConfig().setEnumType(ConfigPaths.WHITELISTMODE_KEY, value);
    }

    @Override
    public Class<EventWhitelistType> getEnumClass() {
        return EventWhitelistType.class;
    }
}
