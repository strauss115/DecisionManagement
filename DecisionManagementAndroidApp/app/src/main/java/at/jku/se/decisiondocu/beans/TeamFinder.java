package at.jku.se.decisiondocu.beans;

import java.util.List;

import at.jku.se.decisiondocu.restclient.client.model.Project;

/**
 * Created by martin on 24.11.15.
 */
public interface TeamFinder {

    List<Project> findAll();
}
