package org.texttechnologylab.project.uebung2.xml.data;

import java.util.List;


/**
 * Dieses Interface enthält Methoden, um mit einer Liste von Reden zu arbeiten und nicht mit nur einer einzelnen Rede
 * @author arthurwunder
 */
public interface RedenListe {

    /**
     * Fügt eine Rede zu der RedenListe hinzu
     * @param rede (Rede)
     * @author arthurwunder
     */
    void addRede(Rede rede);

    /**
     * Gibt alle Reden aus.
     * @return List<Rede>
     * @author arthurwunder
     */
    List<Rede> getAlleReden();

}
