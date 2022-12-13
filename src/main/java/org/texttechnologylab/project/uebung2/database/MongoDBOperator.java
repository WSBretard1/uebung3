package org.texttechnologylab.project.uebung2.database;

import com.mongodb.MongoWriteException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.texttechnologylab.project.uebung2.xml.data.*;

import java.io.IOException;


/**
 * Diese Klasse enthält Methoden zum Uploaden und Zählen von Daten in der mongoDB
 * @author arthurwunder
 */
public class MongoDBOperator {

    MongoDBConnectionHandler mongo = new MongoDBConnectionHandler("src/main/resources/PRG_WiSe22_359.txt");

    /**
     * Konstruktor, kann Daten in die Datenbank hochladen.
     * @param redenListe (RedenListe)
     * @param rednerListe (RednerListe)
     * @param kontrolle (boolean)
     * @throws IOException
     * @author arthurwunder
     */
    public MongoDBOperator(RedenListe redenListe, RednerListe rednerListe, boolean kontrolle) throws IOException {
        if (kontrolle){
            uploadReden(redenListe);
            uploadRedner(rednerListe);
        }
    }

    /**
     * Methode zum Hochladen der Reden in die jeweilige Datenbank.
     * @param redenListe RedenListe
     * @author arthurwunder
     */
    private void uploadReden(RedenListe redenListe) {

        for (Rede rede : redenListe.getAlleReden()) {
            try {
                mongo.getRedenCollection().insertOne(rede.getBasicDBObject());

            } catch (MongoWriteException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Methode zum Hochladen der Redner in die jeweilige Datenbank.
     * @param rednerListe RednerListe
     * @author arthurwunder
     */
    private void uploadRedner(RednerListe rednerListe) {
        for (Redner redner : rednerListe.getAlleRedner()) {
            mongo.getRednerCollection().insertOne(redner.getBasicDBObject());
        }
    }

    /**
     * Diese Methode zählt alle Redner in der Collection Redner, leider keine Zeit mehr gehabt sie in der Konsole abzufragen.
     * müsste aber funktionieren
     * @return int
     * @author arthurwunder
     */
    public int countAlleRedner() {
        FindIterable<Document> rednerIt = mongo.getRednerCollection().find();
        MongoCursor<Document> cursor = rednerIt.cursor();
        int c = 0;
        while (cursor.hasNext()) {
            c += 1;
            cursor.next();
        }
        return c;
    }


    /**
     * Diese Methode zählt alle Reden in der Collection Reden, leider keine Zeit mehr gehabt sie in der Konsole abzufragen.
     * müsste aber funktionieren
     * @return int
     * @author arthurwunder
     */
    public int countAlleReden() {
        FindIterable<Document> redenIt = mongo.getRedenCollection().find();
        MongoCursor<Document> cursor = redenIt.cursor();

        int c = 0;
        while (cursor.hasNext()) {
            c += 1;
            cursor.next();
        }
        return c;
    }
}
