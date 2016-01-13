package at.jku.se.decisiondocu.views;

import android.content.Context;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.w3c.dom.Text;

import java.net.URL;

import at.jku.se.decisiondocu.R;
import at.jku.se.decisiondocu.chat.ChatInterface;
import at.jku.se.decisiondocu.login.SaveSharedPreference;
import at.jku.se.decisiondocu.restclient.client.model.MsgWrapper;
import at.jku.se.decisiondocu.restclient.client.model.NodeInterface;

/**
 * Created by martin on 30.11.15.
 */
@EViewGroup(R.layout.viewgroup_chat)
public class ChatListItemView extends LinearLayout {

    @ViewById(R.id.chat_message_layout)
    LinearLayout layout;

    @ViewById(R.id.chat_message_text)
    TextView tv_headline;

    @ViewById(R.id.chat_message_author)
    TextView tv_author;

    @ViewById(R.id.chat_message_timestamp)
    TextView tv_timestamp;

    private ChatInterface chatInterface;
    private NodeInterface nodeInterface;

    public ChatListItemView(Context context) {
        super(context);
    }

    public ChatListItemView(Context context, BaseAdapter adapter) {
        this(context);
    }

    /**
     * Binds the massage to the chat
     * @param item
     * @param chatInterface
     */
    public void bind(MsgWrapper item, ChatInterface chatInterface) {

        this.chatInterface = chatInterface;

        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;

        if (item.getCreatorEmail() != null && item.getCreatorEmail().equals(SaveSharedPreference.getUserEmail(getContext()))) {
            layout.setBackgroundResource(R.drawable.speech_bubble_green);
            lp.gravity = Gravity.RIGHT;

        } else if (item.getCreatorEmail() != null && item.getCreatorEmail().equals("chatadmin@example.com")) {
            layout.setBackgroundResource(R.drawable.speech_bubble_blue);
            lp.gravity = Gravity.LEFT;
        } else {
            layout.setBackgroundResource(R.drawable.speech_bubble_orange);
            lp.gravity = Gravity.LEFT;
        }

        layout.setLayoutParams(lp);
        tv_headline.setText(item.getMessage());
        tv_author.setText(item.getCreator());
        tv_timestamp.setText(item.getTimestamp().yyyyMMdd());

        // Node auswerten
        if (item.getNode() == null) {

        } else {
            NodeInterface node = item.getNode();
            this.nodeInterface = node;

            String html = "<a href='" + node.getId() + "'>" + node.getId() + " (" + node.getName() + ") created!" + "</a>";
            setTextViewHTML(tv_headline, html);
        }
    }

    private void setTextViewHTML(TextView text, String html) {
        CharSequence sequence = Html.fromHtml(html);
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(sequence);
        URLSpan[] urls = stringBuilder.getSpans(0, sequence.length(), URLSpan.class);
        for (URLSpan span : urls) {
            makeLinkClickable(stringBuilder, span);
        }
        text.setText(stringBuilder);
        text.setLinksClickable(true);
        text.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void makeLinkClickable(SpannableStringBuilder stringBuilder, final URLSpan span) {
        int start = stringBuilder.getSpanStart(span);
        int end = stringBuilder.getSpanEnd(span);
        int flags = stringBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Log.d("span clicked", span.getURL());
                if (chatInterface != null) {
                    chatInterface.linkClicked(nodeInterface.getId(), nodeInterface);
                }
            }
        };
        stringBuilder.setSpan(clickable, start, end, flags);
        stringBuilder.removeSpan(span);
    }
}
