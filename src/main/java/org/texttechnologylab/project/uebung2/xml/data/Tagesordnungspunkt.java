package org.texttechnologylab.project.uebung2.xml.data;

import org.bson.Document;

/**
 * Dieses Interface enthält Methoden, um mit dem Tagesordnungspunkt im Bundestagsprotokoll zu arbeiten.
 * @author arthurwunder
 */
public interface Tagesordnungspunkt {

    /**
     * Gibt das Protokoll zum entsprechenden tagesordnungspunkt aus.
     * @return Document
     * @author arthurwunder
     */
    Document getProtokoll();

    /**
     * Gibt das Dokument (für MongoDB) vom Objekt aus.
     * @return Document
     * @author arthurwunder
     */
    Document getBasicDBObject();

}
