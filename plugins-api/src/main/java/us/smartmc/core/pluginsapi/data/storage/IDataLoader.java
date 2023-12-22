package us.smartmc.core.pluginsapi.data.storage;

import org.bson.Document;

public interface IDataLoader {

    Document load();
    Document save();
}
