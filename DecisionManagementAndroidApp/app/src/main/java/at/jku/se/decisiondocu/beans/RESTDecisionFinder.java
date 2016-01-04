package at.jku.se.decisiondocu.beans;

import org.androidannotations.annotations.EBean;

import java.util.List;

import at.jku.se.decisiondocu.beans.interfaces.DecisionFinder;
import at.jku.se.decisiondocu.restclient.RestClient;
import at.jku.se.decisiondocu.restclient.client.model.Decision;

/**
 * Created by martin on 08.12.15.
 *
 * Implements the DecisionFinder interface.
 * Calls REST Methods for finding Decision Objects.
 */
@EBean
public class RESTDecisionFinder implements DecisionFinder {

    @Override
    public List<Decision> findAll() {
        return RestClient.getAllDecisions();
    }

    @Override
    public Decision find(long id) { return RestClient.getDecisionWithId(id); }
}
