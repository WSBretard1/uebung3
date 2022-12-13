package org.texttechnologylab.project.uebung2.xml;


import org.texttechnologylab.project.uebung2.nlp.NLPPipeline;
import org.texttechnologylab.project.uebung2.xml.data.*;
import org.texttechnologylab.project.uebung2.xml.data.impl.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


/**
 * Diese Klasse enthält Methoden zum Parsen der XML-Dateien und initialisiert die MongoDB
 * @author arthurwunder
 */
public class XMLParser {

    private List<Protokoll> protokolle = new ArrayList<>();
    private RednerListe rednerListe = new RednerListeImplementation();
    private RedenListe redenListe = new RedenListeImplementation();
    private List<Tagesordnungspunkt> tagesordnungspunkte = new ArrayList<>();

    /**
     * Konstruktor der Klasse
     * @param pfad (String)
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @throws ParseException
     * @author arthurwunder
     */
    public XMLParser(String pfad) throws ParserConfigurationException, IOException, SAXException, ParseException {
        NLPPipeline nlp = new NLPPipeline();

        run(pfad, nlp);
    }

    /**
     * Diese Methode geht durch alle Dateien im Ordner und parst die Dateien.
     * Sie kreiert Objekte für jede wichte Information mithilfe von extractRedner und extractReden.
     *
     * @param pfad (String)
     * @param nlp
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @author arthurwunder
     */
    private void run(String pfad, NLPPipeline nlp) throws ParserConfigurationException, IOException, SAXException, ParseException {

        File ordner = new File(pfad);

        System.out.println("-- Dies könnte einen Moment dauern --");
        for (File file : ordner.listFiles()) {

            if (file.getName().equals("dbtplenarprotokoll.dtd")) {
                continue;
            }
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            document.getDocumentElement().normalize();

            Protokoll protokoll = new Protokoll_MongoDB_Impl();

            //kreiert legislaturperiode
            NodeList head = document.getElementsByTagName("wahlperiode");
            protokoll.setLegislaturperiode(head);

            //kreiert sitzungsnummer
            head = document.getElementsByTagName("sitzungsnr");
            protokoll.setSitzungsnummer(head);

            //kreiert Datum
            head = document.getElementsByTagName("datum");
            protokoll.setDatum(head);

            //kreiert redner
            head = document.getElementsByTagName("rednerliste");
            Node nodeHead = head.item(0);
            if (nodeHead.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nodeHead;
                NodeList rednerListe = element.getElementsByTagName("redner");
                extractRedner(rednerListe);
            }

            //kreiert Tagesordnungspunkte
            NodeList tagesordnungspunkte = document.getElementsByTagName("tagesordnungspunkt");
            for (int i = 0; i < tagesordnungspunkte.getLength(); i++) {
                Node top = tagesordnungspunkte.item(i);
                if (top.getNodeType() == Node.ELEMENT_NODE) {
                    Element tagesop = (Element) top;
                    String topID = tagesop.getAttribute("top-id");
                    Tagesordnungspunkt tagesordnungspunkt = new Tagesordnungspunkt_MongoDB_Impl(topID, protokoll);

                    //kreiert Reden
                    NodeList neueReden = top.getChildNodes();
                    tagesordnungspunkt = extractReden(tagesordnungspunkt, neueReden, nlp, protokoll);
                    this.tagesordnungspunkte.add(tagesordnungspunkt);
                }
            }

            protokolle.add(protokoll);
        }


    }


    /**
     * Diese Methode implementiert das Parsen für einen Redner.
     * Für jeden Redner wird ein Redner Objekt erstellt.
     * @param rednerNodeListe (NodeList)
     * @author arthurwunder
     */
    private void extractRedner(NodeList rednerNodeListe) {

        for (int i = 0; i < rednerNodeListe.getLength(); i++) {

            Node nodeRedner = rednerNodeListe.item(i);
            if (nodeRedner.getNodeType() == Node.ELEMENT_NODE) {
                Element elementRedner = (Element) nodeRedner;

                String id  = elementRedner.getAttribute("id");

                if(!rednerListe.getIDs().contains(id)) {
                    String vorname = elementRedner.getElementsByTagName("vorname").item(0).getTextContent();
                    String nachname = elementRedner.getElementsByTagName("nachname").item(0).getTextContent();
                    NodeList fraktionNodeListe = elementRedner.getElementsByTagName("fraktion");
                    if (fraktionNodeListe.getLength() > 0) {
                        Redner redner = new Redner_MongoDB_Impl(id, vorname, nachname, fraktionNodeListe.item(0).getTextContent());
                        rednerListe.addRedner(redner);
                    }else{
                        Redner redner = new Redner_MongoDB_Impl(id, vorname, nachname, "fraktionslos");
                        rednerListe.addRedner(redner);
                    }
                }
            }
        }
    }

    /**
     * Diese Methode implementiert das Parsen von Reden.
     * Für jede Rede wird ein neues Rede-Objekt kreiert.
     *
     * @param tagesordnungspunkt (Tagesordnungspunkt)
     * @param neueReden          (NodeList)
     * @param nlp
     * @return tagesordnungspunkt
     * @author arthurwunder
     */
    private Tagesordnungspunkt extractReden(Tagesordnungspunkt tagesordnungspunkt, NodeList neueReden, NLPPipeline nlp, Protokoll protokoll) {
        for (int j = 0; j < neueReden.getLength(); j++) {

            Node nodeRede = neueReden.item(j);
            if (nodeRede.getNodeName().equals("rede")) {
                String redenID = "";

                ArrayList<org.bson.Document> listeReden = new ArrayList<>();
                ArrayList<org.bson.Document> listeKommentare = new ArrayList<>();

                Element elementRede = (Element) nodeRede;
                if (elementRede.hasAttribute("id")) {
                    redenID = elementRede.getAttribute("id");

                    // Reden, Index und Kommentare mithilfe von "J", "0", "j_1" aus den XML-Dateien extrahieren
                    NodeList nodes = elementRede.getChildNodes();

                    int index = 0;
                    for (int i = 0; i < nodes.getLength(); i++) {
                        Node wert = nodes.item(i);

                        if (wert.getNodeName().equals("kommentar")) {
                            org.bson.Document indexKommentare = new org.bson.Document();
                            indexKommentare.put("text", wert.getTextContent());
                            indexKommentare.put("index", index++);
                            listeKommentare.add(indexKommentare);
                        } else if (wert.getNodeName().equals("p")) {
                            Element element = (Element) wert;
                            if ((element.getAttribute("klasse").equals("J")) ||
                                    (element.getAttribute("klasse").equals("O")) ||
                                    (element.getAttribute("klasse").equals("J_1"))) {
                                org.bson.Document indexRede = new org.bson.Document();
                                indexRede.put("text", wert.getTextContent());
                                indexRede.put("index", index++);
                                listeReden.add(indexRede);
                            }
                        }
                    }
                }

                //Hier ähnlich zu oben den inhalt der Rede extrahieren
                String redenText = "";


                NodeList redenTextListe = elementRede.getElementsByTagName("p");
                for (int y = 0; y < redenTextListe.getLength(); y++) {
                    Node redeTextNode = redenTextListe.item(y);
                    if (redeTextNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element elementRedeText = (Element) redeTextNode;
                        if ((elementRedeText.getAttribute("klasse").equals("J")) ||
                                (elementRedeText.getAttribute("klasse").equals("O")) ||
                                (elementRedeText.getAttribute("klasse").equals("J_1"))) {
                            redenText = redenText.concat(elementRedeText.getTextContent());
                        }
                    }
                }

                //den Redner der Rede mit seiner ID aus dem Dokument auslesen
                NodeList redner1 = elementRede.getElementsByTagName("redner");
                Node nodeRedner = redner1.item(0);
                if (nodeRedner == null) {
                    continue;
                }
                String rednerID = ((Element) nodeRedner).getAttribute("id");
                Redner redner = rednerListe.getRedner(rednerID);
                if (redner == null){
                    continue;
                }
                Rede rede = new Rede_MongoDB_Impl(redenID, redenText, redner.getRednerID(), tagesordnungspunkt, nlp, protokoll, redner, listeReden, listeKommentare);

                redenListe.addRede(rede);
                redner.addRede(rede);
            }
        }
        return tagesordnungspunkt;
    }


    /**
     * Diese Methode macht eine Rednerliste die in MongoDBOperator benutzt wird
     * @return rednerListe
     * @author arthurwunder
     */
    public RednerListe getRednerListe() {
        return rednerListe;
    }

    /**
     * Diese Methode macht eine Liste aller Reden die in MongoDBOperator benutzt wird
     * @return redenListe
     * @author arthurwunder
     */
    public RedenListe getRedenListe() {
        return redenListe;
    }
}