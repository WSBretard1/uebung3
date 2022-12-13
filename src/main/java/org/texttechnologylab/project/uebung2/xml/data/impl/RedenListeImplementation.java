package org.texttechnologylab.project.uebung2.xml.data.impl;

import org.texttechnologylab.project.uebung2.xml.data.Rede;
import org.texttechnologylab.project.uebung2.xml.data.RedenListe;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse implementiert ein Set für alle existierenden Reden, um nicht nur mit einzelnen Reden arbeiten zu können.
 * @author arthurwunder
 */

public class RedenListeImplementation implements RedenListe {
    private List<Rede> alleReden = new ArrayList<>();

    /**
     * Fügt eine Rede zu der RedenListe hinzu.
     * @param rede (Rede)
     * @author arthurwunder
     */
    @Override
    public void addRede(Rede rede){
        alleReden.add(rede);
    }

    /**
     * Gibt alle Reden aus.
     * @return List<Rede>
     * @author arthurwunder
     */
    @Override
    public List<Rede> getAlleReden() {
        return alleReden;
    }
}
