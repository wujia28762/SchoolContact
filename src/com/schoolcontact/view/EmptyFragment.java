package com.schoolcontact.view;

import com.school_contact.main.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EmptyFragment extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return onInit(inflater, container);
	}
	@Override
	public View onInit(LayoutInflater inflater, ViewGroup container) {
		
		
		View tc = inflater.inflate(R.layout.fragment_loading, container, false);
		
		return tc;
	}

}
