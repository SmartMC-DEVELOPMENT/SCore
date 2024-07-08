package us.smartmc.backend.protocol;

import us.smartmc.backend.instance.filetransfer.FileTransferType;

import java.io.File;

public interface IFileRegistrar {

    String getOriginPath();
    String getDestinationPath();

    String getFileName();

    FileTransferType getType();
    long getId();

    default File getDestinationFile() {
        if (getType() == FileTransferType.CACHE) {
            return new File(System.getProperty("java.io.tmpdir"), getFileName());
        } else {
            return new File(getDestinationPath());
        }
    }
}
