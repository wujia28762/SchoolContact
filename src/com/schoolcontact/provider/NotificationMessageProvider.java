package com.schoolcontact.provider;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.school_contact.main.R;
import com.schoolcontact.message.NotificationMessage;

@ProviderTag(messageContent = NotificationMessage.class, showPortrait = false)
public  class NotificationMessageProvider extends IContainerItemProvider.MessageProvider<NotificationMessage> {
    @Override
    public void bindView(View v, int position, NotificationMessage content, Message message) {
        ViewHolder viewHolder = (ViewHolder) v.getTag();

        if (content != null) {
            if (!TextUtils.isEmpty(content.getContent()))
                viewHolder.contentTextView.setText(content.getContent());
        }

    }

    @Override
    public Spannable getContentSummary(NotificationMessage data) {
        if (data != null && !TextUtils.isEmpty(data.getContent()))
            return new SpannableString(data.getContent());
        return null;
    }

    @Override
    public void onItemClick(View view, int position, NotificationMessage
            content, Message message) {
    }

    @Override
    public void onItemLongClick(View view, int i, NotificationMessage contactNotificationMessage, Message message) {

    }

    @Override
    public View newView(Context context, ViewGroup group) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_message_provider_item, group,false);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.contentTextView = (TextView) view.findViewById(R.id.notification_provider_content);
        viewHolder.contentTextView.setMovementMethod(LinkMovementMethod.getInstance());
        view.setTag(viewHolder);

        return view;
    }


    class ViewHolder {
        TextView contentTextView;
    }

}
