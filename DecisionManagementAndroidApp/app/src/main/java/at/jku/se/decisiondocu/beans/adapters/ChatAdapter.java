package at.jku.se.decisiondocu.beans.adapters;

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
 *
 * Adapter, used for the ChatActivity.
 * It contains a list of MsgWrapper Objects that represent a single chat message
 *
 */
@EBean
public class ChatAdapter extends BaseAdapter {

    private final ObjectMapper mapper = new ObjectMapper();
    private ChatInterface chatInterface;
    private ArrayList<MsgWrapper> mListItems;

    @RootContext
    Context context;

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

    /**
     * Sets chatInterface
     * @param chatInterface
     */
    public void setChatInterface(ChatInterface chatInterface) {
        this.chatInterface = chatInterface;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCount() {
        return mListItems.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MsgWrapper getItem(int i) {
        return mListItems.get(i);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getItemId(int i) {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
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
