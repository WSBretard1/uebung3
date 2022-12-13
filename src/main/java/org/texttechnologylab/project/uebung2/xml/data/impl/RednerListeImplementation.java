package org.texttechnologylab.project.uebung2.xml.data.impl;


import org.texttechnologylab.project.uebung2.xml.data.Redner;
import org.texttechnologylab.project.uebung2.xml.data.RednerListe;

import java.util.HashSet;
import java.util.Set;

/**
 * Diese Klasse implementiert ein Set für alle existierenden Redner.
 * @author arthurwunder
 */
public class RednerListeImplementation implements RednerListe {

    private Set<Redner> alleRedner = new HashSet<>();
    private Set<String> ids = new HashSet<>();

    /**
     * Fügt einen Redner zur Rednerliste hinzu.
     * @param redner (Redner)
     * @author arthurwunder
     */
    @Override
    public void addRedner(Redner redner) {
        alleRedner.add(redner);
        ids.add(redner.getRednerID());
    }

    /**
     * Gibt einen Redner nach seiner ID aus.
     * @param id (String)
     * @return Redner
     * @author arthurwunder
     */
    @Override
    public Redner getRedner(String id) {
        for (Redner redner : alleRedner) {
            if (redner.getRednerID().equals(id)) {
                return redner;
            }
        }
        return null;
    }

    /**
     * Gibt alle Redner aus.
     * @return Set<Redner>
     * @author arthurwunder
     */
    @Override
    public Set<Redner> getAlleRedner() {
        return alleRedner;
    }

    /**
     * Gibt alle IDs der Redner aus.
     * @return Set<String>
     * @author arthurwunder
     */
    @Override
    public Set<String> getIDs() {
        return ids;
    }
}
