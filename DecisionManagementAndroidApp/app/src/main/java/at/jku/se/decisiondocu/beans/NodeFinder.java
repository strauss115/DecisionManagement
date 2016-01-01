package at.jku.se.decisiondocu.beans;

import at.jku.se.decisiondocu.restclient.client.model.NodeInterface;

/**
 * Created by martin on 08.12.15.
 */
public interface NodeFinder {

    NodeInterface find(long id);
}
