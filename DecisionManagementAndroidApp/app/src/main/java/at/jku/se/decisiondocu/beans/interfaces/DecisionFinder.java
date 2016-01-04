package at.jku.se.decisiondocu.beans.interfaces;

import java.util.List;

import at.jku.se.decisiondocu.restclient.client.model.Decision;

/**
 * Created by martin on 08.12.15.
 */
public interface DecisionFinder {

    /**
     * Returns all available Decision Objects
     * @return
     */
    List<Decision> findAll();

    /**
     * Returns a certain Decision Object
     * @param id id of the Decision to find
     * @return
     */
    Decision find(long id);
}
