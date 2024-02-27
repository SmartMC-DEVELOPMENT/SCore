package us.smartmc.gamesmanager.player.annotations;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import us.smartmc.gamesmanager.util.MongoDBUtilities;
import us.smartmc.gamesmanager.util.StringUtils;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.stream.Stream;

public interface GamePlayerData {

    Object getId();

    default Document getAsDocument() {
        final Document document = new Document();

        final Class<?> clazz = this.getClass();
        for (Field declaredField : clazz.getDeclaredFields()) {
            declaredField.setAccessible(true);

            if (!declaredField.isAnnotationPresent(MongoPlayerData.class)) continue;

            final MongoPlayerData annotation = declaredField.getAnnotation(MongoPlayerData.class);
            final String rawKey = annotation.name().equals("[null]")
                    ? declaredField.getName()
                    : annotation.name();

            try {
                String key = StringUtils.toSnakeCase(rawKey);
                if (key.equals("uuid")) key = "_id";

                document.put(key, declaredField.get(this));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return document;
    }

    default void loadFromDocument(Document document) {
        for (Map.Entry<String, Object> value : document.entrySet()) {
            final String key = StringUtils.toCamelCase(value.getKey());
            try {
                if (key.equals("_id")) continue;

                Field field = null;
                for (Field filterField : this.getClass().getDeclaredFields()) {
                    if (!filterField.isAnnotationPresent(MongoPlayerData.class)) continue;
                    final MongoPlayerData annotation = filterField.getAnnotation(MongoPlayerData.class);

                    final String rawDbName = annotation.name().equals("[null]")
                            ? filterField.getName()
                            : annotation.name();
                    final String dbName = StringUtils.toSnakeCase(rawDbName);

                    if (dbName.equals(key)) field = filterField;
                }
                if (field == null) continue;
                field.setAccessible(true);

                field.set(this, value.getValue());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    default Document getQueryDocument() {
        return new Document().append("_id", this.getId().toString());
    }

    default void load() {
        if (this.skip()) return;
        final Document queryDocument = this.getQueryDocument();

        final MongoCollection<Document> mongoCollection = this.getMongoCollection();
        if (mongoCollection == null) return;
        final Document document = mongoCollection.find(queryDocument).first();

        if (document != null) this.loadFromDocument(document);
    }

    default void save() {
        if (this.skip()) return;

        final Document queryDocument = this.getQueryDocument();
        final Document updateDocument = new Document("$set", this.getAsDocument());

        final MongoCollection<Document> mongoCollection = this.getMongoCollection();
        if (mongoCollection == null) return;
        mongoCollection.updateOne(queryDocument, updateDocument);
    }

    default boolean skip() {
        return Stream.of(this.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(MongoPlayerData.class))
                .count() <= 1;
    }

    @Nullable
    default MongoCollection<Document> getMongoCollection() {
        if (this.getDatabase() == null || this.getCollection() == null) return null;
        return MongoDBUtilities.collection(this.getDatabase(), this.getCollection());
    }

    default String getDatabase() {
        return null;
    }

    default String getCollection() {
        return null;
    }
}
