package com.schoolcontact.view;


import com.schoolcontact.Base.MessageCallback;
import com.schoolcontact.Base.ScContext;
import com.schoolcontact.model.ReturnMessage;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public abstract class BaseFragment extends Fragment implements OnClickListener,OnItemClickListener,MessageCallback {

	protected ScContext sccontext = ScContext.getInstance();
	
	@Override
	public void dealMessage(ReturnMessage rm) {
		
	}

	@Override
	public void onClick(View v) {
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
	}

	public abstract View onInit(LayoutInflater inflater, ViewGroup container);
}
