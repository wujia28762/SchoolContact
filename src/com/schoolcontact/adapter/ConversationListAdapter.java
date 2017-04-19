package com.schoolcontact.adapter;

import io.rong.imkit.R;
import io.rong.imkit.RLog;
import io.rong.imkit.RongContext;
import io.rong.imkit.model.ConversationProviderTag;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.widget.AsyncImageView;
import io.rong.imkit.widget.ProviderContainerView;
import io.rong.imkit.widget.adapter.BaseAdapter;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Conversation;
import me.add1.resource.Resource;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ConversationListAdapter extends BaseAdapter<UIConversation> {
	LayoutInflater mInflater;
	Context mContext;

	public long getItemId(int position) {
		UIConversation conversation = (UIConversation) getItem(position);
		if (conversation == null)
			return 0L;
		return conversation.hashCode();
	}

	public ConversationListAdapter(Context context) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(this.mContext);
	}

	public int findPosition(Conversation.ConversationType type, String targetId) {
		int index = getCount();
		int position = -1;
		if (RongContext.getInstance()
				.getConversationGatherState(type.getName()).booleanValue() == true) {
			do
				if (index-- <= 0)
					break;
			while (!((UIConversation) getItem(index)).getConversationType()
					.equals(type));
			position = index;
		} else {
			while (index-- > 0) {
				if ((!((UIConversation) getItem(index)).getConversationType()
						.equals(type))
						|| (!((UIConversation) getItem(index))
								.getConversationTargetId().equals(targetId)))
					continue;
				position = index;
			}

		}

		return position;
	}

	protected View newView(Context context, int position, ViewGroup group) {
		View result = this.mInflater.inflate(R.layout.rc_item_conversation,
				null);

		ViewHolder holder = new ViewHolder();
		holder.layout = findViewById(result, R.id.rc_item_conversation);
		holder.leftImageLayout = findViewById(result, R.id.rc_item1);
		holder.rightImageLayout = findViewById(result, R.id.rc_item2);
		holder.leftImageView = ((AsyncImageView) findViewById(result,
				R.id.rc_left));
		holder.rightImageView = ((AsyncImageView) findViewById(result,
				R.id.rc_right));
		holder.contentView = ((ProviderContainerView) findViewById(result,
				R.id.rc_content));
		holder.unReadMsgCount = ((TextView) findViewById(result,
				R.id.rc_unread_message));
		holder.unReadMsgCountRight = ((TextView) findViewById(result,
				R.id.rc_unread_message_right));

		result.setTag(holder);

		return result;
	}

	protected void bindView(View v, int position, UIConversation data) {
		ViewHolder holder = (ViewHolder) v.getTag();

		IContainerItemProvider provider = RongContext.getInstance()
				.getConversationTemplate(data.getConversationType().getName());

		View view = holder.contentView.inflate(provider);

		provider.bindView(view, position, data);

		if (data.isTop())
			holder.layout.setBackgroundColor(this.mContext.getResources()
					.getColor(R.color.rc_conversation_top_bg));
		else {
			holder.layout.setBackgroundColor(0);
		}
		ConversationProviderTag tag = RongContext.getInstance()
				.getConversationProviderTag(
						data.getConversationType().getName());

		if (tag.portraitPosition() == 1) {
			holder.leftImageLayout.setVisibility(0);

			if ((data.getConversationType()
					.equals(Conversation.ConversationType.GROUP))
					|| (data.getConversationType()
							.equals(Conversation.ConversationType.DISCUSSION)))
				holder.leftImageView.setDefaultDrawable(v.getContext()
						.getResources()
						.getDrawable(R.drawable.rc_group_default_portrait));
			else {
				holder.leftImageView.setDefaultDrawable(v.getContext()
						.getResources()
						.getDrawable(R.drawable.rc_default_portrait));
			}

			if (data.getIconUrl() != null)
				holder.leftImageView
						.setResource(new Resource(data.getIconUrl()));
			else {
				holder.leftImageView.setResource(null);
			}

			if (data.getUnReadMessageCount() > 0) {
				holder.unReadMsgCount.setVisibility(0);
				if (data.getUnReadMessageCount() > 99)
					holder.unReadMsgCount.setText(this.mContext.getResources()
							.getString(R.string.rc_message_unread_count));
				else
					holder.unReadMsgCount.setText(Integer.toString(data
							.getUnReadMessageCount()));
			} else {
				holder.unReadMsgCount.setVisibility(4);
			}
			holder.rightImageLayout.setVisibility(8);
		} else if (tag.portraitPosition() == 2) {
			holder.rightImageLayout.setVisibility(0);

			if ((data.getConversationType()
					.equals(Conversation.ConversationType.GROUP))
					|| (data.getConversationType()
							.equals(Conversation.ConversationType.DISCUSSION)))
				holder.rightImageView.setDefaultDrawable(v.getContext()
						.getResources()
						.getDrawable(R.drawable.rc_group_default_portrait));
			else {
				holder.rightImageView.setDefaultDrawable(v.getContext()
						.getResources()
						.getDrawable(R.drawable.rc_default_portrait));
			}

			if (data.getIconUrl() != null)
				holder.rightImageView.setResource(new Resource(data
						.getIconUrl()));
			else {
				holder.rightImageView.setResource(null);
			}

			if (data.getUnReadMessageCount() > 0) {
				holder.unReadMsgCountRight.setVisibility(0);
				if (data.getUnReadMessageCount() > 99)
					holder.unReadMsgCountRight.setText(this.mContext
							.getResources().getString(
									R.string.rc_message_unread_count));
				else {
					holder.unReadMsgCountRight.setText(Integer.toString(data
							.getUnReadMessageCount()));
				}
			}

			holder.leftImageLayout.setVisibility(8);
		} else if (tag.portraitPosition() == 3) {
			holder.rightImageLayout.setVisibility(8);
			holder.leftImageLayout.setVisibility(8);
		} else {
			throw new IllegalArgumentException(
					"the portrait position is wrong!");
		}

		RLog.d(this, "leftImageLayout", "position:" + position + " Visibility:"
				+ holder.leftImageLayout.getVisibility());
	}

	class ViewHolder {
		View layout;
		View leftImageLayout;
		View rightImageLayout;
		AsyncImageView leftImageView;
		TextView unReadMsgCount;
		AsyncImageView rightImageView;
		TextView unReadMsgCountRight;
		ProviderContainerView contentView;

		ViewHolder() {
		}
	}
}