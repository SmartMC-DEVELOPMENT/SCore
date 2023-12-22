package us.smartmc.core.pluginsapi.data.storage;

import org.bson.Document;

public abstract class DocumentLoader extends Document implements IDataLoader {

    private final Document queryDocument;

    public DocumentLoader(String path, Object value) {
        queryDocument = new Document().append(path, value);
    }

    public Document getQueryDocument() {
        return queryDocument;
    }
}
