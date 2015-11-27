package at.jku.se.decisiondocu.beans;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 24.11.15.
 */
@EBean
public class InMemoryTeamFinder implements TeamFinder {

    @Override
    public List<Team> findAll() {

        Team t1 = new Team("Project 1");
        t1.setTeamDecisionCount(21);
        t1.setTeamImageUrl("http://www.keenthemes.com/preview/metronic/theme/assets/global/plugins/jcrop/demos/demo_files/image1.jpg");
        Team t2 = new Team("Project 2");
        t2.setTeamDecisionCount(2);
        t2.setTeamImageUrl("http://www.menucool.com/slider/jsImgSlider/images/image-slider-2.jpg");
        Team t3 = new Team("Project 3");
        t3.setFavourite(true);
        t3.setTeamDecisionCount(0);
        t3.setTeamImageUrl("http://crackberry.com/sites/crackberry.com/files/styles/large/public/topic_images/2013/ANDROID.png");


        List<Team> mItems = new ArrayList<>();

        mItems.add(t1);
        mItems.add(t2);
        mItems.add(t3);

        return mItems;
    }
}
