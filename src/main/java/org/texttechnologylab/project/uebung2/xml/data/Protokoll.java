package org.texttechnologylab.project.uebung2.xml.data;

import org.bson.Document;
import org.w3c.dom.NodeList;

import java.text.ParseException;
import java.time.LocalDate;

/**
 * Dieses Interface enthält Methoden, um mit dem Protokoll zu arbeiten
 * @author arthurwunder
 */
public interface Protokoll {

    /**
     * Legt das Datum des Protokolls fest.
     * @param head (NodeList)
     * @author arthurwunder
     */
    void setDatum(NodeList head) throws ParseException;

    /**
     * Legt die Legislaturperiode des Protokolls fest.
     * @param head (NodeList)
     * @author arthurwunder
     */
    void setLegislaturperiode(NodeList head);

    /**
     * Legt die Sitzungsnummer des Protokolls fest.
     * @param head (NodeList)
     * @author arthurwunder
     */
    void setSitzungsnummer(NodeList head);

    /**
     * Gibt das Datum des Protokolls aus.
     * @return LocalDate
     * @author arthurwunder
     */
    LocalDate getDatum();

    /**
     * Gibt die Kalenderwoche des Protokolls aus.
     * @return int
     * @author arthurwunder
     */
    int getKalenderwoche();

    /**
     * Gibt den Monat des Protokolls aus.
     * @return int
     * @author arthurwunder
     */
    int getMonat();

    /**
     * Gibt die Legislaturperiode des Protokolls aus.
     * @return String
     * @author arthurwunder
     */
    String getLegislaturperiode();

    /**
     * Gibt die Sitzungsnummer des Protokolls aus.
     * @return String
     * @author arthurwunder
     */
    String getSitzungsnummer();

    /**
     * Gibt das Dokument (für MongoDB) vom Objekt aus.
     * @return Document
     * @author arthurwunder
     */
    Document getBasicDBObject();

}
