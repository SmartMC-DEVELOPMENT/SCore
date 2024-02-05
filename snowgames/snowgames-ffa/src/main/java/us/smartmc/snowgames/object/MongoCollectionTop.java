package us.smartmc.snowgames.object;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import lombok.Getter;
import lombok.Setter;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MongoCollectionTop {

    private final MongoCollection<Document> mongoCollection;
    @Getter
    private final String documentPath;

    @Setter
    @Getter
    private int rankingAmountToGet = 10;

    public MongoCollectionTop(String dbName, String collectionName, String documentPath) {
        // Conexión a MongoDB
        MongoDatabase database = MongoDBConnection.mainConnection.getDatabase(dbName);
        this.mongoCollection = database.getCollection(collectionName);
        this.documentPath = documentPath;
    }

    public List<Document> getTopScores() {
        // Obtener top scores desde MongoDB
        return mongoCollection.find()
                .sort(Sorts.descending(documentPath))
                .limit(rankingAmountToGet)
                .into(new ArrayList<>());
    }

    public long getPlayerRank(String uuid) {
        Document playerScore = mongoCollection.find(Filters.eq("_id", uuid)).first();
        if (playerScore == null) {
            return -1; // Jugador no encontrado
        }

        Number score = playerScore.get(documentPath, Number.class);
        return mongoCollection.countDocuments(Filters.gte(documentPath, score));
    }
}
