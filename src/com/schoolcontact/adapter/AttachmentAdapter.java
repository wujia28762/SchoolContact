package com.schoolcontact.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.school_contact.main.R;
import com.schoolcontact.model.ItemAttachment;

public class AttachmentAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<ItemAttachment> data;
	
	public AttachmentAdapter(Context context, List<ItemAttachment> resData) {
		super();
		this.inflater = LayoutInflater.from(context);
		this.data = resData;
	}
	
	@Override
	public int getCount() {
		return data.size();
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
	            convertView = inflater.inflate(R.layout.attachment_item, null); 
	            holder.attachmentView = (TextView) convertView.findViewById(R.id.attachment_content); 
	            holder.deleteButton = (Button) convertView.findViewById(R.id.attachment_delete); 
	            convertView.setTag(holder);
	        } else {
	            holder = (ViewHolder) convertView.getTag(); 
	        } 
	        holder.attachmentView.setText("Ãû³Æ£º"+data.get(position).getFile_name());
	        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
	            @Override 
	            public void onClick(View v) {
	            	data.remove(position);
	            	AttachmentAdapter.this.notifyDataSetChanged();
	            } 
	        }); 
	        return convertView; 
	}
	
	class ViewHolder{
		TextView attachmentView;
		Button deleteButton;
	}

}
