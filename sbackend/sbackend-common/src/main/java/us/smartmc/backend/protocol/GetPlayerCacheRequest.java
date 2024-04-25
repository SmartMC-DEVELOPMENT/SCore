package us.smartmc.backend.protocol;

import lombok.Getter;

import java.util.UUID;

@Getter
public class GetPlayerCacheRequest {

    private final UUID id;

    public GetPlayerCacheRequest(UUID uuid) {
        this.id = uuid;
    }
}
