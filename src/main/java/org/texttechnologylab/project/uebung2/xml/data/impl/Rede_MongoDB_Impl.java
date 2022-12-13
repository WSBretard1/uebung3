package org.texttechnologylab.project.uebung2.xml.data.impl;


import org.apache.uima.jcas.JCas;
import org.bson.Document;
import org.texttechnologylab.project.uebung2.nlp.NLPPipeline;
import org.texttechnologylab.project.uebung2.xml.data.Protokoll;
import org.texttechnologylab.project.uebung2.xml.data.Rede;
import org.texttechnologylab.project.uebung2.xml.data.Redner;
import org.texttechnologylab.project.uebung2.xml.data.Tagesordnungspunkt;

import java.util.ArrayList;


/**
 * Diese Klasse implementiert ein MongoDB dokument für eine Rede.
 * @author arthurwunder
 */
public class Rede_MongoDB_Impl implements Rede {

    private Document basicDBObject = new Document();

    /**
     * Konstruktor der Klasse.
     * Packt Informationen über eine Rede in ein Dokument.
     *
     * @param redenID            (String)
     * @param redenText          (String)
     * @param rednerID           (String)
     * @param tagesordnungspunkt (Tagesordnungspunkt)
     * @param nlp
     * @param protokoll          (Protokoll)
     * @param redner             (Redner)
     * @param redenListe         (ArrayList<Document>)
     * @param KommentarListe     (ArrayList<Document>)
     * @author arthurwunder
     */
    public Rede_MongoDB_Impl(String redenID, String redenText, String rednerID, Tagesordnungspunkt tagesordnungspunkt,
                             NLPPipeline nlp, Protokoll protokoll, Redner redner, ArrayList<Document> redenListe, ArrayList<Document> KommentarListe) {
        basicDBObject.put("_id", redenID);
        basicDBObject.put("redenText", redenListe);
        basicDBObject.put("redner", rednerID);
        basicDBObject.put("kommentare", KommentarListe);
        basicDBObject.put("datum", protokoll.getDatum());
        basicDBObject.put("kalenderwoche", protokoll.getKalenderwoche());
        basicDBObject.put("monat", protokoll.getMonat());
        basicDBObject.put("partei", redner.getPartei());
        basicDBObject.put("tagesordnungspunkt", tagesordnungspunkt.getBasicDBObject());
        JCas jCas = nlp.analyse(redenText);
        Document sentimentResult = nlp.getSentimentScore(jCas);
        basicDBObject.put("sentiment", sentimentResult.get("sentimentScore"));
        basicDBObject.put("sentences", sentimentResult.get("sentimentDocs"));
        basicDBObject.put("POS", nlp.getPOS(jCas));
        basicDBObject.put("token", nlp.getToken(jCas));
        basicDBObject.put("namedEntities", nlp.getNamedEntities(jCas));
        basicDBObject.put("DDCTopicAnalysis", nlp.getTopic(jCas));
    }

    /**
     * Gibt die ID einer Rede aus.
     * @return String
     * @author arthurwunder
     */
    @Override
    public String getRedenID() {
        return basicDBObject.getString("_id");
    }

    /**
     * Gibt den Inhalt/Text einer Rede aus.
     * @return String
     * @author arthurwunder
     */
    @Override
    public String getRedenText() {
        return basicDBObject.getString("redenText");
    }

    /**
     * Gibt die ID eines Redners einer bestimmten Rede aus.
     * @return String
     * @author arthurwunder
     */
    @Override
    public String getRednerID() {
        return (String) basicDBObject.get("redner");
    }

    /**
     * Gibt den tagesordnungspunkt, indem die Rede ist aus.
     * @return Document
     * @author arthurwunder
     */
    @Override
    public Document getTagesordnungspunkt() {
        return (Document) basicDBObject.get("tagesordnungspunkt");
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
