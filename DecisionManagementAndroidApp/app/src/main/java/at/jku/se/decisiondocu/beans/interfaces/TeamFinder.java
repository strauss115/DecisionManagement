package at.jku.se.decisiondocu.beans.interfaces;

import java.util.List;

import at.jku.se.decisiondocu.restclient.client.model.Project;

/**
 * Created by martin on 24.11.15.
 */
public interface TeamFinder {

    /**
     * Returns all available Project Objects
     * @return
     */
    List<Project> findAll();

    /**
     * Returns all available Project Objects for a certain user
     * @return
     */
    List<Project> find();
}
