package org.texttechnologylab.project.uebung2;

import org.texttechnologylab.project.uebung2.database.MongoDBOperator;
import org.texttechnologylab.project.uebung2.xml.XMLParser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;


/**
 * Diese Klasse beinhaltet die Main Methode zum Durchlaufen des Programms
 * @author arthurwunder
 */

public class Main {

    /**
     * Diese Methode legt die Struktur des Programms fest
     * @param args (String[])
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @throws ParseException
     * @author arthurwunder
     */
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, ParseException {

        String directoryName = "src/main/resources/file";
        File directory = new File(directoryName);
        if (!directory.exists()) {
            System.out.println("Fehler... Bitte legen Sie folgenden Ordner an: " + directoryName);
            System.exit(0);
        }
        String eingabe;
        do {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Haben Sie die Daten schon in der Mongo Datenbank? [ja/nein] Wenn Sie l eingeben löscht sich alles in der Datenbank. SPAß!");
            eingabe = scanner.next();
            if (eingabe.equals("ja")) {
                MongoDBOperator mongo = new MongoDBOperator(null, null, false);
            } else if (eingabe.equals("nein")) {
                System.out.println("Haben Sie die XML-Dateien schon in dem Ordner " + directoryName + "? [ja/nein]");
                eingabe = scanner.next();
                if (eingabe.equals("ja")) {
                    XMLParser parser = new XMLParser(directoryName);
                    MongoDBOperator mongo = new MongoDBOperator(parser.getRedenListe(), parser.getRednerListe(), true);
                } else if (eingabe.equals("nein")) {
                    XMLParser parser = new XMLParser(directoryName);
                    MongoDBOperator mongo = new MongoDBOperator(parser.getRedenListe(), parser.getRednerListe(), true);
                } else {
                    System.out.println("Fehlerhafte Eingabe. Neustart...");
                }
            } else {
                System.out.println("Fehlerhafte Eingabe. Neustart...");
            }
        } while (!(eingabe.equals("ja") || eingabe.equals("nein")));
    }
}
