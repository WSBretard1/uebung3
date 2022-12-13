package org.texttechnologylab.project.uebung2.xml.data.impl;

import org.bson.Document;
import org.texttechnologylab.project.uebung2.xml.data.Rede;
import org.texttechnologylab.project.uebung2.xml.data.Redner;

/**
 * Diese Klasse implementiert ein MongoDB dokument für einen Redner.
 * @author arthurwunder
 */
public class Redner_MongoDB_Impl implements Redner {

    private Document basicDBObject = new Document();
    private Integer anzahlReden = 0;

    /**
     * Konstruktor, fügt Informationen über einen Redner in ein Dokument hinzu.
     * @param id (String)
     * @param vorname (String)
     * @param nachname (String)
     * @param partei (String)
     * @author arthurwunder
     */
    public Redner_MongoDB_Impl(String id, String vorname, String nachname, String partei) {
        basicDBObject.put("_id", id);
        basicDBObject.put("vorname", vorname);
        basicDBObject.put("nachname", nachname);
        basicDBObject.put("istAbgeordneter", id.startsWith("1100"));
        basicDBObject.put("anzahlReden", anzahlReden);
        basicDBObject.put("partei", partei);
    }


    /**
     * Gibt die RednerID aus.
     * @return String
     * @author arthurwunder
     */
    @Override
    public String getRednerID() {
        return basicDBObject.getString("_id");
    }

    /**
     * Gibt den Vornamen eines Redners aus.
     * @return String
     * @author arthurwunder
     */
    @Override
    public String getVorname() {
        return basicDBObject.getString("vorname");
    }

    /**
     * Gibt den Nachnamen eines Redners aus.
     * @return String
     * @author arthurwunder
     */
    @Override
    public String getNachname() {
        return basicDBObject.getString(("nachname"));
    }

    /**
     * Gibt true aus, wenn ein Redner ein Abgeordneter ist.
     * @return boolean
     * @author arthurwunder
     */
    @Override
    public boolean istAbgeordneter() {
        return basicDBObject.getBoolean("istAbgeordneter");
    }

    /**
     * Gibt die Partei eines Redners aus.
     * @return String
     * @author arthurwunder
     */
    @Override
    public String getPartei() {
        return basicDBObject.getString("partei");
    }

    /**
     * Fügt eine Rede zu der totalen Anzahl von Reden eines Redners hinzu.
     * @param rede (Rede)
     * @author arthurwunder
     */
    @Override
    public void addRede(Rede rede) {
        anzahlReden += 1;
        basicDBObject.replace("anzahlReden", anzahlReden);
    }

    /**
     * Gibt das Dokument (für MongoDB) vom Objekt aus.
     * @return Document
     * @author arthurwunder
     */
    @Override
    public Document getBasicDBObject() {
        return basicDBObject;
    }

}
