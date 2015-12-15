package at.jku.se.decisiondocu.beans;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;

import at.jku.se.decisiondocu.views.ChatListItemView;
import at.jku.se.decisiondocu.views.ChatListItemView_;

/**
 * Created by martin on 14.12.15.
 */
@EBean
public class ChatAdapter extends BaseAdapter {

    @RootContext
    Context context;

    private ArrayList<String> mListItems;

    @AfterInject
    void initAdapter() {
        mListItems = new ArrayList<>();
    }

    public void appendData(String data) {
        mListItems.add(data);
        notifyDataSetChanged();
    }

    void setData(ArrayList<String> data) {
        mListItems = data;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        //getCount() represents how many items are in the list
        return mListItems.size();
    }

    @Override
    //get the data of an item from a specific position
    //i represents the position of the item in the list
    public String getItem(int i) {
        return mListItems.get(i);
    }

    @Override
    //get the position id of the item from the list
    public long getItemId(int i) {
        return 0;
    }

    @Override

    public View getView(int position, View convertView, ViewGroup parent) {

        ChatListItemView view;

        if (convertView == null) {
            view = ChatListItemView_.build(context, this);
        } else {
            view = (ChatListItemView) convertView;
        }

        view.bind(getItem(position));
        return view;

    }
}
