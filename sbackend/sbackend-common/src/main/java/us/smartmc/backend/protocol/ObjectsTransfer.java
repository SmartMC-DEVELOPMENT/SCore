package us.smartmc.backend.protocol;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class ObjectsTransfer implements Serializable {

    private final Object[] objects;

    public ObjectsTransfer(Object... initialObjects) {
        this.objects = initialObjects;
    }

}
