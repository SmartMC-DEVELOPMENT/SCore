package us.smartmc.lobbycosmetics.instance.cosmetic;

import lombok.Getter;

public enum CosmeticRarity {

    COMMON('b'), RARE('a'), UNUSUAL('b'), EPIC('5'), LEGENDARY('6');

    @Getter
    final char color;

    CosmeticRarity(char color) {
        this.color = color;
    }

}
