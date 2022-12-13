package org.texttechnologylab.project.uebung2.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

/**
 * Diese Klasse enthält Methoden, um die Verbindung zur jeweiligen Datenbank herzustellen.
 * @author arthurwunder
 */
public class MongoDBConnectionHandler {

    private Properties properties;
    private MongoCredential credential;
    private MongoClientOptions options;
    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection<Document> reden;
    private MongoCollection<Document> redner;

    /**
     * Constructor for the class.
     * Konstruktor der Klasse
     * @param pfadname (String)
     * @throws IOException
     * @author arthurwunder
     */
    public MongoDBConnectionHandler (String pfadname) throws IOException {

        this.properties = new Properties();
        this.properties.load(new FileInputStream(pfadname));

        String propHost = properties.getProperty("remote_host");
        String propDB = properties.getProperty("remote_database");
        String propUser = properties.getProperty("remote_user");
        String propPassword = properties.getProperty("remote_password");
        String propPort = properties.getProperty("remote_port");
        String propCollection = properties.getProperty("remote_collection");

        this.credential = MongoCredential.createCredential(propUser, propDB, propPassword.toCharArray());
        this.options = MongoClientOptions.builder().serverSelectionTimeout(200000).sslEnabled(false).build();
        this.client = new MongoClient(new ServerAddress(propHost, Integer.parseInt(propPort)), Arrays.asList(this.credential), this.options);
        this.database = this.client.getDatabase(propDB);
        this.reden = this.database.getCollection(propCollection);
        if (this.database.getCollection("redner") == null) {
            this.database.createCollection("redner");
        } else {this.redner = this.database.getCollection("redner");}

        System.out.println("Verbindung zum Client hergestellt");
    }

    /**
     * Gibt Collection Reden aus.
     * @return reden (MongoCollection<Document>)
     * @author arthurwunder
     */
    public MongoCollection<Document> getRedenCollection() {
        return reden;
    }

    /**
     * Gibt Collection Redner aus.
     * @return redner (MongoCollection<Document>)
     * @author arthurwunder
     */
    public MongoCollection<Document> getRednerCollection() {
        return redner;
    }

}
