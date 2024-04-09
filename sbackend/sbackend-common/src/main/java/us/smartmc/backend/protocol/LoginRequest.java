package us.smartmc.backend.protocol;

import lombok.Getter;

@Getter
public record LoginRequest(String username, String password) {

}
