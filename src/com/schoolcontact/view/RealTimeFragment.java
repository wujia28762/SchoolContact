package com.schoolcontact.view;

import io.rong.imkit.RongIM;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.school_contact.main.R;
import com.schoolcontact.widget.BadgeView;

public class RealTimeFragment extends BaseFragment {

	private BadgeView badge1;
	private ImageView pyqs_enter;
	private TranslateAnimation enterAnima;
	private TranslateAnimation exitAnima;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	public View onInit(LayoutInflater inflater, ViewGroup container) {

		View v = inflater.inflate(R.layout.tab_realtime, container, false);

		RelativeLayout rlchatone = (RelativeLayout) v
				.findViewById(R.id.RLchatone);
		RelativeLayout rlgroup = (RelativeLayout) v.findViewById(R.id.RLgroup);
		RelativeLayout rlfriends = (RelativeLayout) v
				.findViewById(R.id.RLfriends);
		enterAnima = new TranslateAnimation(0, 0, 100, 0);
		enterAnima.setInterpolator(new BounceInterpolator());
		enterAnima.setDuration(1000);
		exitAnima = new TranslateAnimation(0, 0, 0, -100);
		exitAnima.setInterpolator(new BounceInterpolator());
		exitAnima.setDuration(1000);
		pyqs_enter = (ImageView) (v.findViewById(R.id.pyqs_enter));
		badge1 = new BadgeView(this.getActivity(), pyqs_enter);
		
		if(sccontext.isNewAboutMe())
		{
			//badge1 = new BadgeView(this.getActivity(), tv_pyq);
			// badge1.
			badge1.setText(sccontext.getmCustomContent().size()+"");
			badge1.setBadgePosition(BadgeView.POSITION_BOTTOM_LEFT); //默认值
			//badge1.setGravity(Gravity.LEFT);
			badge1.show(enterAnima);
		}
		else if(sccontext.isNewMessage())
		{
			badge1.setText(R.string.newmessgae);
			badge1.setBadgePosition(BadgeView.POSITION_BOTTOM_LEFT); //默认值
			//badge1.setGravity(Gravity.LEFT);
			badge1.show(enterAnima);
		}
		rlchatone.setOnClickListener(this);
		rlgroup.setOnClickListener(this);
		rlfriends.setOnClickListener(this);

		return v;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return onInit(inflater, container);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.RLchatone: {
			// RongIM.getInstance().startConversationList(this.getActivity());

			if(badge1.isShown())
			{
				badge1.hide(exitAnima);
			}
//			Intent pyq = new Intent(this.getActivity(), PyqActivity.class);
//			this.startActivity(pyq);
			sccontext.setNewMessage(false);
			Intent mainIntent = new Intent(this.getActivity(),
					PyqActivity.class);
			this.startActivity(mainIntent);
			
			
			break;
		}

		}
	}

}
