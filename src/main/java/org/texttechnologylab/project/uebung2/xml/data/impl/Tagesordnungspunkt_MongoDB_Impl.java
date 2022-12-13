package org.texttechnologylab.project.uebung2.xml.data.impl;

import org.bson.Document;
import org.texttechnologylab.project.uebung2.xml.data.Protokoll;
import org.texttechnologylab.project.uebung2.xml.data.Tagesordnungspunkt;


/**
 * Diese Klasse implementiert ein MongoDB dokument für einen Tagesordnungspunkt
 * @author arthurwunder
 */
public class Tagesordnungspunkt_MongoDB_Impl implements Tagesordnungspunkt {

    private Document basicDBObject = new Document();

    /**
     * Konstruktor der Klasse, legt Informationen über den Tagesordnungspunkt in ein MongoDB Dokument.
     * @param tagesordnungspunktID (String)
     * @param protokoll (Protokoll)
     * @author arthurwunder
     */
    public Tagesordnungspunkt_MongoDB_Impl(String tagesordnungspunktID, Protokoll protokoll){

        basicDBObject.put("textId", tagesordnungspunktID);
        basicDBObject.put("protokoll", protokoll.getBasicDBObject());
    }

    /**
     * Gibt das Protokoll von einem Tagesordnungspunkt aus.
     * @return Document
     * @author arthurwunder
     */
    @Override
    public Document getProtokoll() {
        return (Document) basicDBObject.get("protokoll");
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
