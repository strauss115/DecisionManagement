package at.jku.se.decisiondocu.views;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import at.jku.se.decisiondocu.R;

/**
 * Created by martin on 30.11.15.
 */
@EViewGroup(R.layout.list_item)
public class ChatListItemView extends LinearLayout {

    @ViewById(R.id.list_item_text_view)
    TextView tv_headline;


    public ChatListItemView(Context context) {
        super(context);
    }

    public ChatListItemView(Context context, BaseAdapter adapter) {
        this(context);
    }

    public void bind(String item) {
        tv_headline.setText(item);
    }
}
