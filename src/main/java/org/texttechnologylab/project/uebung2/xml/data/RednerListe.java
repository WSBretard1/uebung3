package org.texttechnologylab.project.uebung2.xml.data;

import java.util.Set;

/**
 * Dieses Interface enthält Methoden, um mit allen Rednern zu arbeiten und nicht nur mit einem Einzigen.
 * @author arthurwunder
 */
public interface RednerListe {

    /**
     * Fügt einen Redner zu der Rednerliste hinzu.
     * @param redner (Redner)
     * @author arthurwunder
     */
    void addRedner(Redner redner);

    /**
     * Gibt Redner nach seiner entsprechenden ID aus.
     * @param id (String)
     * @return Redner
     * @author arthurwunder
     */
    Redner getRedner(String id);

    /**
     * Gibt alle Redner aus
     * @return Set<Redner>
     * @author arthurwunder
     */
    Set<Redner> getAlleRedner();

    /**
     * Gibt alle IDs von allen Rednern aus.
     * @return Set<String>
     * @author arthurwunder
     */
    Set<String> getIDs();

}
