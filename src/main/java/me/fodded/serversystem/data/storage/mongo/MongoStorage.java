package me.fodded.serversystem.data.storage.mongo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import me.fodded.serversystem.data.impl.PlayerData;
import me.fodded.serversystem.data.storage.IDataStorage;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Methods from this class must be called only in the DataManager classes, not anywhere else
 */
public class MongoStorage implements IDataStorage {

    private MongoClient mongoClient;
    private MongoCollection<Document> playersCollection;

    private static final Gson GSON = new GsonBuilder().create();

    @Override
    public void connect() {
        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017/?uuidRepresentation=STANDARD");
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                pojoCodecRegistry);

        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();

        mongoClient = MongoClients.create(clientSettings);
        playersCollection = mongoClient.getDatabase("serversystem").getCollection("playerStatistics");
    }

    @Override
    public CompletableFuture<PlayerData> loadData(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            Document foundDocument = playersCollection.find(Filters.eq("uuid", uuid.toString())).first();
            if(foundDocument == null) {
                return new PlayerData(uuid);
            }

            return GSON.fromJson(foundDocument.toJson(), PlayerData.class);
        });
    }

    @Override
    public CompletableFuture<Void> saveData(PlayerData playerData) {
        return CompletableFuture.runAsync(() -> {
            String json = GSON.toJson(playerData);
            Document foundPlayerDocument = playersCollection.find(new Document("uuid", playerData.getUuid().toString())).first();
            if(foundPlayerDocument == null) {
                playersCollection.insertOne(Document.parse(json));
                return;
            }

            playersCollection.replaceOne(foundPlayerDocument, Document.parse(json));
        });
    }
}
