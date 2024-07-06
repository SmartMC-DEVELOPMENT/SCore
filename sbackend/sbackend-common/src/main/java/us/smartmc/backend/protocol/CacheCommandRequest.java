package us.smartmc.backend.protocol;

import lombok.Getter;
import us.smartmc.backend.instance.cache.CacheCommand;
import us.smartmc.backend.instance.cache.CacheCommandType;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

public class CacheCommandRequest implements Serializable {

    private final int typeIndex;
    private final byte[] id;
    private final byte[] key;

    @Getter
    private Object value;

    public CacheCommandRequest(CacheCommand command) {
        this.typeIndex = command.getType().ordinal();
        this.id = command.getId().getBytes(StandardCharsets.UTF_8);
        this.key = command.getKey().getBytes(StandardCharsets.UTF_8);
        this.value = command.getValue();
    }

    public CacheCommandType getType() {
        return CacheCommandType.values()[typeIndex];
    }

    public String getId() {
        return new String(id, StandardCharsets.UTF_8);
    }

    public String getKey() {
        return new String(key, StandardCharsets.UTF_8);
    }

    public static CacheCommand getCacheCommandFromRequest(CacheCommandRequest cmd) {
        return CacheCommand
                .build(cmd.getType(), cmd.getKey())
                .value(cmd.getValue());
    }

}
