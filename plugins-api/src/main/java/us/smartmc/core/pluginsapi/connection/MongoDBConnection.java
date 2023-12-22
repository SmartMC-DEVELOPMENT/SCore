package us.smartmc.core.pluginsapi.connection;

import com.mongodb.*;

public class MongoDBConnection extends MongoClient {

    public static MongoDBConnection mainConnection;

    public MongoDBConnection(String host, int port) {
        super(host, port);
    }

    public MongoDBConnection(String url) {
        super(new MongoClientURI(url));
    }

    public MongoDBConnection(MongoClientURI uri) {
        super(uri);
    }

    public MongoDBConnection(ServerAddress address, MongoCredential credential, MongoClientOptions options) {
        super(address, credential, options);
    }

}
