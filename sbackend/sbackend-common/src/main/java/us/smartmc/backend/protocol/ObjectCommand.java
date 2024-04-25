package us.smartmc.backend.protocol;

import com.google.gson.Gson;

import java.io.Serializable;

public class ObjectCommand implements Serializable {

    private final byte[] className;
    private final byte[] jsonObject;

    public ObjectCommand(Object object) {
        this.className = object.getClass().getName().getBytes();
        this.jsonObject = new Gson().toJson(object).getBytes();
    }

    public <T> T getObject(Class<? extends T> clazz) {
        return new Gson().fromJson(readFrom(jsonObject), clazz);
    }

    public Class<?> getTypeClass() throws ClassNotFoundException {
        return Class.forName(readFrom(className));
    }

    private static String readFrom(byte[] a) {
        return new String(a);
    }

}
