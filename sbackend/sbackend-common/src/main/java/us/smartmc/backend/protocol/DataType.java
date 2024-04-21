package us.smartmc.backend.protocol;

import lombok.Getter;

@Getter
public enum DataType {

    UTF((byte) 0x01), OBJECT((byte) 0x02);

    final byte id;

    DataType(byte id) {
        this.id = id;
    }

    public static DataType getValueOf(byte a) {
        for (DataType type : values()) {
            if (type.id == a) return type;
        }
        return null;
    }

}
