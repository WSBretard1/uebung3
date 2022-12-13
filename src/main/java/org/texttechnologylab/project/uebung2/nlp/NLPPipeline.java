package org.texttechnologylab.project.uebung2.nlp;


import au.com.bytecode.opencsv.CSVReader;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.bson.Document;
import org.hucompute.textimager.fasttext.labelannotator.LabelAnnotatorDocker;
import org.hucompute.textimager.uima.gervader.GerVaderSentiment;
import org.hucompute.textimager.uima.spacy.SpaCyMultiTagger3;
import org.hucompute.textimager.uima.type.Sentiment;
import org.hucompute.textimager.uima.type.category.CategoryCoveredTagged;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static org.apache.commons.math.util.MathUtils.round;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;


/**
 * Diese Klasse implementiert Methoden um die NLP Analyse durchzuführen.
 * @author arthurwunder
 */
public class NLPPipeline {

    private AnalysisEngine pAE = null;
    private HashMap<String, String> csvData;


    /**
     * Konstruktor der Klasse, hier wird die Pipeline kreiert.
     * @author Guiseppe Abrami
     * @modified arthurwunder
     */
    public NLPPipeline(){

        readCSV();

        AggregateBuilder builder = new AggregateBuilder();

        try {
            builder.add(createEngineDescription(SpaCyMultiTagger3.class,
                    SpaCyMultiTagger3.PARAM_REST_ENDPOINT,
                    "http://spacy.lehre.texttechnologylab.org"
            ));

            String sPOSMapFile = "src/main/resources/am_posmap.txt";

            builder.add(createEngineDescription(LabelAnnotatorDocker.class,
                    LabelAnnotatorDocker.PARAM_FASTTEXT_K, 100,
                    LabelAnnotatorDocker.PARAM_CUTOFF, false,
                    LabelAnnotatorDocker.PARAM_SELECTION, "text",
                    LabelAnnotatorDocker.PARAM_TAGS, "ddc3",
                    LabelAnnotatorDocker.PARAM_USE_LEMMA, true,
                    LabelAnnotatorDocker.PARAM_ADD_POS, true,
                    LabelAnnotatorDocker.PARAM_POSMAP_LOCATION, sPOSMapFile,
                    LabelAnnotatorDocker.PARAM_REMOVE_FUNCTIONWORDS, true,
                    LabelAnnotatorDocker.PARAM_REMOVE_PUNCT, true,
                    LabelAnnotatorDocker.PARAM_REST_ENDPOINT, "http://ddc.lehre.texttechnologylab.org"
            ));

            builder.add(createEngineDescription(GerVaderSentiment.class,
                    GerVaderSentiment.PARAM_REST_ENDPOINT, "http://gervader.lehre.texttechnologylab.org",
                    GerVaderSentiment.PARAM_SELECTION, "text,de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence"
            ));

            this.pAE = builder.createAggregate();
        } catch (ResourceInitializationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Diese Methode konvertiert einen String in ein JCas Objekt
     * @param text (String)
     * @return (JCas)
     * @author arthurwunder
     */
    public JCas toCas(String text) {
        JCas jCas = null;
        try {
            jCas = JCasFactory.createText(text, "de");

        } catch (UIMAException e) {
            e.printStackTrace();
        }
        return jCas;
    }

    /**
     * Diese Methode ist zum Durchlaufen der Pipeline.
     * @param text (String)
     * @return (jCas)
     * @author arthurwunder
     */
    public JCas analyse(String text) {

        JCas jCas = toCas(text);
        try {
            SimplePipeline.runPipeline(jCas, pAE);
        } catch (UIMAException e) {
            e.printStackTrace();
        }
        return jCas;
    }

    /**
     * Diese Methode identifiziert das POS in einem String und speichert die Anzahl der Erscheinungen.
     * @param jCas (JCas)
     * @return (Document)
     * @author arthurwunder
     */
    public Document getPOS(JCas jCas) {


        Document posDocs = new Document();
        ArrayList<Document> posList = new ArrayList<>();

        Collection<POS> poses = JCasUtil.select(jCas, POS.class);
        for (POS pos : poses) {
            boolean control = true;
            for (Document doc : posList){
                if(doc.get("pos").equals(pos.getPosValue())){
                    Integer count = (Integer) doc.get("count");
                    count++;
                    doc.replace("count", count);
                    control = false;
                    break;
                }
            }
            if(control){
                Document doc = new Document();
                doc.put("pos", String.valueOf(pos.getPosValue()));
                doc.put("count", 1);
                posList.add(doc);
            }

        }
        posDocs.put("result", posList);
        return posDocs;
    }

    /**
     * Diese Methode identifiziert Token in einem String und speichert die Anzahl der Erscheinungen.
     * @param jCas (JCas)
     * @return (Document)
     * @author arthurwunder
     */
    public Document getToken(JCas jCas) {

        Document tokenDocs = new Document();
        ArrayList<Document> tokenList = new ArrayList<>();
        Collection<Token> token = JCasUtil.select(jCas, Token.class);
        for (Token tok : token) {
            boolean control = true;
            for (Document doc : tokenList){
                if(doc.get("token").equals(tok.getLemmaValue())){
                    Integer count = (Integer) doc.get("count");
                    count++;
                    doc.replace("count", count);
                    control = false;
                    break;
                }
            }
            if(control){
                Document doc = new Document();
                doc.put("token", tok.getLemmaValue());
                doc.put("count", 1);
                tokenList.add(doc);
            }
        }
        tokenDocs.put("result", tokenList);
        return tokenDocs;
    }

    /**
     * Diese Methode berechnet den Sentiment Score von einer Rede und der enthaltenen Sätze.
     * @param jCas (JCas)
     * @return (Document)
     * @author arthurwunder
     */
    public Document getSentimentScore(JCas jCas) {

        Document sentimentDocs = new Document();
        ArrayList<Document> sentimentList = new ArrayList<>();
        Collection<Sentiment> sentiments = JCasUtil.select(jCas, Sentiment.class);
        Collection<Sentence> sentences = JCasUtil.select(jCas, Sentence.class);
        double sentimentScore = 0;
        for (Sentence sentence : sentences){
            Document doc = new Document();
            doc.put("sentiment", "");
            doc.put("sentence", sentence.getCoveredText());
            sentimentList.add(doc);

        }
        int j = 0;
        int i = 0;
        for (Sentiment sentiment : sentiments) {


            if (j < 2){
                j++;
                continue;
            }

            sentimentList.get(i).replace("sentiment", String.valueOf(sentiment.getSentiment()));
            sentimentScore += sentiment.getSentiment();
            i++;
            j++;

        }
        if (sentences.size() > 0) {
            sentimentScore = round(sentimentScore / sentences.size(), 4);
        } else {
            sentimentScore = 0.0;
        }

        sentimentDocs.put("result", sentimentList);
        Document result = new Document();
        result.put("sentimentDocs", sentimentDocs);
        result.put("sentimentScore", String.valueOf(sentimentScore));
        return result;
    }

    /**
     * Diese Methode identifiziert Personen, Orte und Organisationen in einem String und speichert die Anzahl an Erscheinungen.
     * @param jCas (JCas)
     * @return (Document)
     * @author arthurwunder
     */
    public Document getNamedEntities(JCas jCas){
        Document namedEntitiesDoc = new Document();
        Document namedEntitiesResult = new Document();
        ArrayList<Document> peopleList = new ArrayList<>();
        ArrayList<Document> organisationsList = new ArrayList<>();
        ArrayList<Document> locationsList = new ArrayList<>();


        Collection<NamedEntity> namedEntities = JCasUtil.select(jCas, NamedEntity.class);

        for (NamedEntity namedEntity : namedEntities) {

            // people

            if (namedEntity.getValue().equals("PER")) {
                boolean control = true;
                for(Document doc : peopleList){
                    if(doc.get("person").equals(String.valueOf(namedEntity.getCoveredText()))){
                        Integer count = (Integer) doc.get("count");
                        count++;
                        doc.replace("count", count);
                        control = false;
                        break;
                    }
                }
                if(control){
                    Document doc = new Document();
                    doc.put("person", String.valueOf(namedEntity.getCoveredText()));
                    doc.put("count", 1);
                    peopleList.add(doc);
                }
            }

            // organisations

            if (namedEntity.getValue().equals("ORG")){
                boolean control = true;
                for(Document doc : organisationsList){
                    if(doc.get("organisation").equals(String.valueOf(namedEntity.getCoveredText()))){
                        Integer count = (Integer) doc.get("count");
                        count++;
                        doc.replace("count", count);
                        control = false;
                        break;
                    }
                }
                if(control){
                    Document doc = new Document();
                    doc.put("organisation", String.valueOf(namedEntity.getCoveredText()));
                    doc.put("count", 1);
                    organisationsList.add(doc);
                }
            }

            // locations

            if (namedEntity.getValue().equals("LOC")){
                boolean control = true;
                for(Document doc : locationsList){
                    if(doc.get("location").equals(String.valueOf(namedEntity.getCoveredText()))){
                        Integer count = (Integer) doc.get("count");
                        count++;
                        doc.replace("count", count);
                        control = false;
                        break;
                    }
                }
                if(control){
                    Document doc = new Document();
                    doc.put("location", String.valueOf(namedEntity.getCoveredText()));
                    doc.put("count", 1);
                    locationsList.add(doc);
                }
            }
        }
        namedEntitiesResult.put("people", peopleList);
        namedEntitiesResult.put("organisations", organisationsList);
        namedEntitiesResult.put("locations", locationsList);
        namedEntitiesDoc.put("result", namedEntitiesResult);

        return namedEntitiesDoc;

    }

    /**
     * Diese Methode berechnet die Titel Scores in einem String und sichert diesen.
     * @param jCas (JCas)
     * @return (Document)
     * @author arthurwunder
     */

    public Document getTopic(JCas jCas) {

        Document topicDoc = new Document();
        ArrayList<Document> topicList = new ArrayList<>();

        Collection<CategoryCoveredTagged> topics = JCasUtil.select(jCas, CategoryCoveredTagged.class);
        for (CategoryCoveredTagged topic : topics) {
            Document doc = new Document();
            String value = topic.getValue();
            String id = value.substring(value.length() - 3);
            doc.put("topic", csvData.get(id));
            doc.put("score", topic.getScore());
            topicList.add(doc);
        }
        topicDoc.put("result", topicList);
        return topicDoc;
    }

    /**
     * Diese Methode liest die .csv datei und sichert die Werte in einer Hashmap.
     * @author arthurwunder
     */

    public void readCSV() {
        csvData = new HashMap<>();
        try {
            FileReader file = new FileReader("src/main/resources/ddc3-names-de.csv");
            CSVReader reader = new CSVReader(file, '\n');
            String [] next;
            while ((next = reader.readNext()) != null) {
               for (String value : next) {
                   if (!value.equals("")) {
                       String number = value.substring(0, 3);
                       String topic = value.substring(4);
                       csvData.put(number, topic);
                   }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


}
