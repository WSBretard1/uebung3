package org.texttechnologylab.project.uebung2.xml.data;

import org.bson.Document;

/**
 * Dieses Interface enthält Methoden, um mit einem Redner zu arbeiten.
 * @author arthurwunder
 */
public interface Redner {

    /**
     * Fügt eine Rede zu der Anzahl gehaltener Reden eines Redners hinzu.
     * @param rede (Rede)
     * @author arthurwunder
     */
    void addRede(Rede rede);

    /**
     * Gibt die RednerID aus.
     * @return String
     * @author arthurwunder
     */
    String getRednerID();

    /**
     * Gibt den Vornamen aus.
     * @return String
     * @author arthurwunder
     */
    String getVorname();

    /**
     * Gibt den Nachnamen aus.
     * @return String
     * @author arthurwunder
     */
    String getNachname();

    /**
     * Gibt aus, ob ein Redner ein Abgeordneter ist.
     * @return boolean
     * @author arthurwunder
     */
    boolean istAbgeordneter();

    /**
     * Gibt die Partei eines Redners aus
     * @return String
     * @author arthurwunder
     */
    String getPartei();


    /**
     * Gibt das Dokument (für MongoDB) vom Objekt aus.
     * @return Document
     * @author arthurwunder
     */
    Document getBasicDBObject();


}
