package org.texttechnologylab.project.uebung2.xml.data.impl;


import org.bson.Document;
import org.texttechnologylab.project.uebung2.xml.data.Protokoll;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;


/**
 * Diese Klasse implementiert ein MongoDB dokument für ein Protokoll.
 * @author arthurwunder
 */
public class Protokoll_MongoDB_Impl implements Protokoll {
    private Document basicDBObject = new Document();

    /**
     * Legt die Sitzungsnummer eines Protokolls fest.
     * @param head (NodeList)
     * @author arthurwunder
     */
    @Override
    public void setSitzungsnummer(NodeList head) {
        Node node = head.item(0);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            basicDBObject.put("sitzungsnummer", element.getTextContent());
        }
    }


    /**
     * Legt die Legislaturperiode eines Protokolls fest.
     * @param head (NodeList)
     * @author arthurwunder
     */
    @Override
    public void setLegislaturperiode(NodeList head) {
        Node node = head.item(0);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            basicDBObject.put("legislaturperiode", element.getTextContent());
        }
    }

    /**
     * Legt das Datum eines Protokolls fest.
     * @param head (NodeList)
     * @throws ParseException
     * @author arthurwunder
     */
    @Override
    public void setDatum(NodeList head) throws ParseException {
        Node node = head.item(0);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;

            DateTimeFormatter deutsch = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate datum = LocalDate.parse(element.getAttribute("date"), deutsch);
            basicDBObject.put("datum", datum);

            int kalenderwoche = datum.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
            int monat = datum.getMonthValue();

            basicDBObject.put("kalenderwoche", kalenderwoche);
            basicDBObject.put("monat", monat);

        }
    }

    /**
     * Gibt das Datum eines Protokolls aus.
     * @return LocalDate
     * @author arthurwunder
     */
    @Override
    public LocalDate getDatum() {
        return (LocalDate) basicDBObject.get("datum");
    }

    /**
     * Gibt die Kalenderwoche eines Protokolls aus.
     * @return int
     * @author arthurwunder
     */
    @Override
    public int getKalenderwoche() {
        return basicDBObject.getInteger("kalenderwoche");
    }

    /**
     * Gibt den Monat eines Protokolls aus.
     * @return int
     * @author arthurwunder
     */
    @Override
    public int getMonat() {
        return basicDBObject.getInteger("monat");
    }

    /**
     * Gibt die Legislaturperiode eines Protokolls aus.
     * @return String
     * @author arthurwunder
     */
    @Override
    public String getLegislaturperiode() {
        return basicDBObject.getString("legislaturperiode");
    }

    /**
     * Gibt die Sitzungsnummer eines Protkolls aus.
     * @return String
     * @author arthurwunder
     */
    @Override
    public String getSitzungsnummer() {
        return basicDBObject.getString("sitzungsnummer");
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
