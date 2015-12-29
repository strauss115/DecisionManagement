package at.jku.se.decisiondocu.beans;

import org.androidannotations.annotations.EBean;

import java.util.List;

import at.jku.se.decisiondocu.restclient.RestClient;
import at.jku.se.decisiondocu.restclient.client.model.Project;

/**
 * Created by martin on 07.12.15.
 */
@EBean
public class RESTProjectByUserFinder implements TeamFinder {

    @Override
    public List<Project> findAll() {
        return RestClient.getMyProjects();
    }

    @Override
    public List<Project> find() {
        return null;
    }
}
