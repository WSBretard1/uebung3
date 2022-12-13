package org.texttechnologylab.project.uebung2.xml.data;

import org.bson.Document;

/**
 * Dieses Interface enthält Methoden, um mit einer Rede zu arbeiten.
 * @author arthurwunder
 */
public interface Rede {

    /**
     * Gibt die ID einer Rede aus.
     * @return String
     * @author arthurwunder
     */
    String getRedenID();

    /**
     * Gibt den Text/Inhalt einer Rede aus.
     * @return String
     * @author arthurwunder
     */
    String getRedenText();

    /**
     * Gibt die ID des Redners der entsprechenden Rede aus.
     * @return String
     * @author arthurwunder
     */
    String getRednerID();

    /**
     * Gibt den Tagesordnungspunkt, in welchem die Rede ist aus.
     * @return Document
     * @author arthurwunder
     */
    Document getTagesordnungspunkt();

    /**
     * Gibt das Dokument (für MongoDB) vom Objekt aus.
     * @return Document
     * @author arthurwunder
     */
    Document getBasicDBObject();
}
