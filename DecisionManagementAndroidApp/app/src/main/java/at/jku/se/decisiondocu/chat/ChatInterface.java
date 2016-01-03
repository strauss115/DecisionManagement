package at.jku.se.decisiondocu.chat;

import at.jku.se.decisiondocu.restclient.client.model.NodeInterface;

/**
 * Created by martin on 03.01.16.
 */
public interface ChatInterface {

    void linkClicked(long id, NodeInterface nodeInterface);
}
