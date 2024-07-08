package us.smartmc.backend.handler;

import us.smartmc.backend.instance.filetransfer.FileTransferType;
import us.smartmc.backend.protocol.IFileRegistrar;

import java.util.HashMap;
import java.util.Map;

public class FileRegistrarManager {

    private static final Map<Long, IFileRegistrar> registries = new HashMap<>();

    public static void register(IFileRegistrar registrar) {
        registries.put(registrar.getId(), registrar);
    }

    public static FileTransferType getTypeOf(long id) {
        return get(id).getType();
    }

    public static IFileRegistrar get(long id) {
        return registries.get(id);
    }

}
