package at.jku.se.decisiondocu.beans;

import org.androidannotations.annotations.EBean;

import at.jku.se.decisiondocu.restclient.RestClient;
import at.jku.se.decisiondocu.restclient.client.model.NodeInterface;

/**
 * Created by martin on 08.12.15.
 */
@EBean
public class RESTNodeFinder implements NodeFinder {

    @Override
    public NodeInterface find(long id) { return RestClient.getNodeWithId(id); }
}
