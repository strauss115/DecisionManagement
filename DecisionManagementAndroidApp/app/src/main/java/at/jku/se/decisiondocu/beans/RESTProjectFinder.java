package at.jku.se.decisiondocu.beans;

import org.androidannotations.annotations.EBean;

import java.util.List;

import at.jku.se.decisiondocu.beans.interfaces.TeamFinder;
import at.jku.se.decisiondocu.restclient.RestClient;
import at.jku.se.decisiondocu.restclient.client.model.Project;

/**
 * Created by martin on 07.12.15.
 *
 * Implements the TeamFinder interface.
 * Calls REST Methods for finding Project Objects.
 */
@EBean
public class RESTProjectFinder implements TeamFinder {

    @Override
    public List<Project> findAll() {
        return RestClient.getAllProjects();
    }

    @Override
    public List<Project> find() {
        return RestClient.getMyProjects();
    }
}
