package com.schoolcontact.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.school_contact.main.R;
import com.schoolcontact.model.AttachmentInfo;
import com.schoolcontact.utils.AsyncImageLoader;

public class NotificationAttachmentAdapter extends BaseAdapter{

	private LayoutInflater inflater;
	private List<AttachmentInfo> data;
	private AsyncImageLoader asyncImageLoader;

	public NotificationAttachmentAdapter(Context context,
			List<AttachmentInfo> resData) {
		super();
		this.inflater = LayoutInflater.from(context);
		this.data = resData;
		asyncImageLoader = new AsyncImageLoader(context);
	}

	@Override
	public int getCount() {
		if(data!=null&&data.size()>0){
			return data.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(
					R.layout.notification_attachment_item, null);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.attachment_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.imageView.setTag(((AttachmentInfo) data.get(position)).getFile_url());
		Bitmap bitmap = asyncImageLoader.loadImage(holder.imageView, ((AttachmentInfo) data.get(position)).getFile_url());
		if (bitmap != null) {
			holder.imageView.setImageBitmap(bitmap);
		}

		return convertView;
	}

	class ViewHolder {
		ImageView imageView;
	}

}
