package us.smartmc.backend.protocol;

import com.google.gson.Gson;

import java.io.*;

public class ObjectCommand implements Serializable {

    private final byte[] serializedObject;

    public ObjectCommand(Object object) throws IOException {
        this.serializedObject = serializeObject(object);
    }

    public <T> T getObject(Class<? extends T> clazz) throws IOException, ClassNotFoundException {
        return deserializeObject(serializedObject, clazz);
    }

    private byte[] serializeObject(Object object) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(object);
        oos.flush();
        return bos.toByteArray();
    }

    private <T> T deserializeObject(byte[] data, Class<? extends T> clazz) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bis);
        return clazz.cast(ois.readObject());
    }

}
