package at.jku.se.decisiondocu.beans;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

import at.jku.se.decisiondocu.restclient.RestClient;

/**
 * Created by martin on 07.12.15.
 */
@EBean
public class RESTProjectFinder implements TeamFinder {

    @Override
    public List<Team> findAll() {
        RestClient.getAllProjects();
        return new ArrayList<>();
    }
}
