package com.schoolcontact.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.school_contact.main.R;
import com.schoolcontact.widget.GridView;

public class HeadCommentGridViewAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private Context context;
	private List<String> persons;
	private String  temp = "";

	public List<String> getPersons() {
		return persons;
	}

	public void setPersons(List<String> persons) {
		this.persons = persons;
	}

	public void add(String p) {
		if (persons != null) {
			persons.add(p);
		}
	}

	public HeadCommentGridViewAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	public HeadCommentGridViewAdapter(Context context, List<String> p) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		persons = p;
	}

	@Override
	public int getCount() {

		if (persons != null)
			return persons.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int arg0) {

		if (persons != null)
			return persons.get(arg0);
		else
			return null;
	}

	@Override
	public long getItemId(int arg0) {

		return arg0;
	}

	public class ViewHolder {

		private TextView likeperson;

	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {

		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.praise_gridview_item, null);
			convertView.setLayoutParams(new GridView.LayoutParams(120, LayoutParams.WRAP_CONTENT));//重点行
			viewHolder = new ViewHolder();
			viewHolder.likeperson = (TextView) convertView
					.findViewById(R.id.tv_likepersons);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if(arg0 == persons.size()-1)
		{
			temp = "";
		}
		else
		{
			temp = ",";
		}
		viewHolder.likeperson.setText(persons.get(arg0)+temp);
		viewHolder.likeperson.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//Toast.makeText(context, "点击了赞姓名！！", Toast.LENGTH_SHORT).show();
			}
		});
		return convertView;
	}
}
