package at.jku.se.decisiondocu.beans;

import java.util.List;

import at.jku.se.decisiondocu.restclient.client.model.Decision;

/**
 * Created by martin on 08.12.15.
 */
public interface DecisionFinder {

    List<Decision> findAll();
    Decision find(long id);
}
