package us.smartmc.backend.protocol;

import lombok.Getter;

@Getter
public class ObjectsTransfer {

    private final Object[] objects;

    public ObjectsTransfer(Object... initialObjects) {
        this.objects = initialObjects;
    }

}
