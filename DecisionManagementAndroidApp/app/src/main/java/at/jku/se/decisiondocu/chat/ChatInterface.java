package at.jku.se.decisiondocu.chat;

import at.jku.se.decisiondocu.restclient.client.model.NodeInterface;

/**
 * Created by martin on 03.01.16.
 */
public interface ChatInterface {

    void messageReceived(String message);

    /**
     * Should get inkoked when a user clicks on a link in a chat message
     *
     * @param id
     * @param nodeInterface
     */
    void linkClicked(long id, NodeInterface nodeInterface);
}
