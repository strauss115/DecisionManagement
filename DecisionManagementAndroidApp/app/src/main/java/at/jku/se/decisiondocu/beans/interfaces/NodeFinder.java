package at.jku.se.decisiondocu.beans.interfaces;

import at.jku.se.decisiondocu.restclient.client.model.NodeInterface;

/**
 * Created by martin on 08.12.15.
 */
public interface NodeFinder {

    /**
     * Returns a certain NodeInterface Object
     * @param id id of the Node to find
     * @return
     */
    NodeInterface find(long id);
}
