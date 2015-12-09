package at.jku.se.decisiondocu.restclient.client.model;

import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 08.12.15.
 */
@JsonTypeName("decisionList")
public class DecisionList {

    private List<Decision> decisions;

    public List<Decision> getList() {
        return decisions;
    }

    public void setList(List<Decision> decisions) {
        this.decisions = decisions;
    }

    public DecisionList(){
        decisions = new ArrayList<Decision>();
    }
}

