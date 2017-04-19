package com.schoolcontact.provider;

import io.rong.imkit.R;
import io.rong.imkit.RLog;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.widget.ArraysDialogFragment;
import io.rong.imkit.widget.AsyncImageView;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ImageMessage;
import me.add1.resource.Resource;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

@ProviderTag(messageContent = ImageMessage.class, showProgress = false)
public class ImageMessageItemProvider extends
		IContainerItemProvider.MessageProvider<ImageMessage> {
	public View newView(Context context, ViewGroup group) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.rc_item_imgmsg, null);

		ViewHolder holder = new ViewHolder();

		holder.message = ((TextView) view.findViewById(R.id.rc_msg));
		holder.img = ((AsyncImageView) view.findViewById(R.id.rc_img));

		view.setTag(holder);
		return view;
	}

	public void onItemClick(View view, int position, ImageMessage content,
			Message message) {
		if (content == null) {
			return;
		} else if(content.getRemoteUri().toString()!=null){
			System.out.println(content.getRemoteUri().toString());
			Uri uri = Uri
					.parse("rong://"
							+ view.getContext().getApplicationInfo().processName)
					.buildUpon()
					.appendPath("showimage")
					.appendQueryParameter("bigimageurl",
							content.getRemoteUri().toString()).build();

			view.getContext().startActivity(
					new Intent("android.intent.action.VIEW", uri));
		}
		

	}

	public void onItemLongClick(View view, int position, ImageMessage content,
			Message message) {
		String name = null;

		final Message msg = message;
		if ((message != null)
				&& (message.getSentStatus().equals(Message.SentStatus.SENDING))) {
			return;
		}
		UserInfo userInfo = (UserInfo) RongContext.getInstance()
				.getUserInfoCache().get(message.getSenderUserId());
		if (userInfo != null)
			name = userInfo.getName();
		String[] items = { view.getContext().getResources()
				.getString(R.string.rc_dialog_item_message_delete) };

		ArraysDialogFragment
				.newInstance(name, items)
				.setArraysDialogItemListener(
						new ArraysDialogFragment.OnArraysDialogItemListener() {
							public void OnArraysDialogItemClick(
									DialogInterface dialog, int which) {
								if (which == 0)
									RongIM.getInstance()
											.getRongIMClient()
											.deleteMessages(
													new int[] { msg
															.getMessageId() },
													null);
							}
						})
				.show(((FragmentActivity) view.getContext())
						.getSupportFragmentManager());
	}

	public void bindView(View v, int position, ImageMessage content,
			Message message) {
		RLog.i(this, "bindView", "index:" + position);

		ViewHolder holder = (ViewHolder) v.getTag();

		if (message.getMessageDirection() == Message.MessageDirection.SEND)
			v.setBackgroundResource(R.drawable.rc_ic_bubble_no_right);
		else {
			v.setBackgroundResource(R.drawable.rc_ic_bubble_no_left);
		}

		holder.img.setResource(content.getThumUri() == null ? null
				: new Resource(content.getThumUri()));

		String extra = message.getExtra();
		int progress = 0;

		if ((extra != null) && (!extra.isEmpty())) {
			progress = Integer.parseInt(extra);
		}
		Message.SentStatus status = message.getSentStatus();

		if ((status.equals(Message.SentStatus.SENDING)) && (progress < 100)) {
			if (progress == 0)
				holder.message.setText("µÈ´ýÖÐ");
			else {
				holder.message.setText(progress + "%");
			}
			holder.message.setVisibility(0);
		} else {
			holder.message.setVisibility(8);
		}
	}

	public Spannable getContentSummary(ImageMessage data) {
		return new SpannableString("[Í¼Æ¬]");
	}

	class ViewHolder {
		AsyncImageView img;
		TextView message;

		ViewHolder() {
		}
	}
}