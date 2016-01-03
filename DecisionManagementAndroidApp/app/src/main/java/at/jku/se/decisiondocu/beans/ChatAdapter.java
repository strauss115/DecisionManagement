package at.jku.se.decisiondocu.beans;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.io.IOException;
import java.util.ArrayList;

import at.jku.se.decisiondocu.chat.ChatInterface;
import at.jku.se.decisiondocu.restclient.client.model.MsgWrapper;
import at.jku.se.decisiondocu.views.ChatListItemView;
import at.jku.se.decisiondocu.views.ChatListItemView_;

/**
 * Created by martin on 14.12.15.
 */
@EBean
public class ChatAdapter extends BaseAdapter {

    private final ObjectMapper mapper = new ObjectMapper();
    private ChatInterface chatInterface;

    @RootContext
    Context context;

    private ArrayList<MsgWrapper> mListItems;

    public void setChatInterface(ChatInterface chatInterface) {
        this.chatInterface = chatInterface;
    }

    @AfterInject
    void initAdapter() {
        mListItems = new ArrayList<>();
    }

    public void appendData(String data) {
        try {
            MsgWrapper msg = mapper.readValue(data, MsgWrapper.class);
            mListItems.add(msg);
            notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setData(ArrayList<MsgWrapper> data) {
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
    public MsgWrapper getItem(int i) {
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

        view.bind(getItem(position), chatInterface);
        return view;

    }
}
